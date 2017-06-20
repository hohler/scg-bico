package tool.bico.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.repodriller.filter.range.CommitRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.scg.metrics.changemetrics.ChangeMetrics;
import ch.unibe.scg.metrics.changemetrics.domain.CMBugRepository;
import ch.unibe.scg.metrics.changemetrics.domain.CMFile;
import ch.unibe.scg.metrics.changemetrics.domain.CMRepository;
import ch.unibe.scg.metrics.sourcemetrics.SourceMetrics;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMCommit;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMFile;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMRepository;
import tool.bico.analysis.BigCommitAnalyzer;
import tool.bico.analysis.CommitAnalyzer;
import tool.bico.controller.form.ChangeMetricFormData;
import tool.bico.controller.form.SingleMetricFormData;
import tool.bico.job.JobCreator;
import tool.bico.model.BigCommit;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.SourceMetric;
import tool.bico.model.SzzMetric;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.model.service.SourceMetricService;
import tool.bico.model.service.SzzMetricService;
import tool.bico.repository.GitRepository;
import tool.bico.utils.CSVUtils;


@SuppressWarnings("unused")
@Controller
@RequestMapping("/projects/{id}/metrics")
public class MetricController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ChangeMetricService changeMetricService;
	
	@Autowired
	private SzzMetricService szzMetricService;
	
	@Autowired
	private SourceMetricService sourceMetricService;

	@Autowired
	private CommitIssueService commitIssueService;
	
	@Autowired
	private CommitService commitService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		List<ChangeMetric> changeMetrics = changeMetricService.getProjectChangeMetrics(project);
		List<SzzMetric> szzMetrics = szzMetricService.getProjectSzzMetrics(project);
		List<SourceMetric> sourceMetrics = sourceMetricService.getProjectSourceMetrics(project);
		
		model.addAttribute("project", project);
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("szzMetrics", szzMetrics);
		model.addAttribute("sourceMetrics", sourceMetrics);
		model.addAttribute("singleMetricHolder", new SingleMetricFormData());
		model.addAttribute("timeWindows", new ChangeMetricFormData().getTimeWindows());
		
		return new ModelAndView("projects/metrics/index", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Object singleMetrics(SingleMetricFormData singleMetricHolder, @PathVariable("id") Long id, BindingResult result,
			RedirectAttributes redirect) throws IOException {
		
		System.setProperty("git.maxdiff", "1000000"); // default was 100'000
		
		Project project = projectService.findById(id);
		
		String ref = singleMetricHolder.getRef();
		// download
		
		if(ref == null || ref.length() < 10) {
			redirect.addFlashAttribute("globalMessage", "Invalid commit ref!");
			return new ModelAndView("redirect:/projects/{project.id}/metrics", "project.id", id);
		}
		
		GitRepository repo = new GitRepository(project, false);
		String path = repo.getRepositoryPath();
		if(path == null) {
			System.err.println("Could not clone repository of project: "+project);
			redirect.addFlashAttribute("globalMessage", "Failed to download metrics of " + ref);
			return new ModelAndView("redirect:/projects/{project.id}/metrics", "project.id", id);
		} else {
			
			if(singleMetricHolder.getExcludeBigCommits()) {
				BigCommitAnalyzer.analyzeBigCommits(project, projectService, commitService);
			}
					
			Commit refCommit = commitService.getCommitByProjectAndRef(project, ref);
			
			List<ChangeMetric> cm = getChangeMetrics(project, path, ref, singleMetricHolder.getTimeWindow(), singleMetricHolder.getExcludeBigCommits());
			
			// wait between, so the repo can be reset
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			List<SourceMetric> sm = getSourceMetrics(project, path, ref);
			
			Commit c = new Commit();
			c.setRef(ref);
			c.setTimestamp(refCommit.getTimestamp());
			c.setChangeMetrics(new HashSet<ChangeMetric>(cm));
			c.setSourceMetrics(new HashSet<SourceMetric>(sm));
			
			List<SzzMetric> szz = szzMetricService.getSzzMetricsByCommit(refCommit);
			c.setSzzMetrics(new HashSet<SzzMetric>(szz));
			
			String fileName = project.getId()+"_"+project.getName()+"_"+ref+"_metrics.csv";
			return downloadData(fileName, new HashSet<Commit>(Arrays.asList(c)));
		}
		
		
		//redirect.addFlashAttribute("globalMessage", "Successfully downloaded metrics of commit " + ref);
		//return new ModelAndView("redirect:/projects/{project.id}/metrics", "project.id", id);
	}
	
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public HttpEntity<byte[]> download(@PathVariable("id") Long id) throws IOException {

		//byte[] documentBody = this.pdfFramework.createPdf(filename);
		
		Project project = projectService.findById(id);
		Set<Commit> commits = project.getCommits();
		
		String fileName = project.getId()+"_"+project.getName()+"_metrics.csv";
		
		return downloadData(fileName, commits);
	}
	
	class MetricHolder {
		SzzMetric szzMetric;
		ChangeMetric changeMetric;
		SourceMetric sourceMetric;
	}
	
	private HttpEntity<byte[]> downloadData(String fileName, Set<Commit> commits) throws IOException {
		StringWriter writer = new StringWriter();
		CSVUtils csv = new CSVUtils(';');
		
		String[] csvHeader = {
			"File",
			"Date",
			"Hash",
			// ChangeMetrics
			"Revisions",
			"Refactorings",
			"Bugfixes",
			"Authors",
			"LOC added",
			"Max LOC added",
			"Avg LOC added",
			"LOC deleted",
			"Max LOC deleted",
			"Avg LOC deleted",
			"Codechurn",
			"Max Codechurn",
			"Avg Codechurn",
			"Max Changeset",
			"Avg Changeset",
			"Age",
			"Weighted Age",
			"# SZZ Bugs",
			"className",
			"Type",
			"CBO",
			"WMC",
			"DIT",
			"NOC",
			"RFC",
			"LCOM",
			"NOM",
			"NOPM",
			"NOSM",
			"NOF",
			"NOPF",
			"NOSF",
			"NOSI",
			"LOC",
			"NOCB",
			"NONC",
			"NONA",
			"NOMWMOP",
			"deleted"
		};
		
		
		csv.writeLine(writer, Arrays.asList(csvHeader));
		
		for(Commit c : commits) {
			
			if(c.getChangeMetrics().size() == 0 || c.getSourceMetrics().size() == 0) continue;
			
			Map<String, MetricHolder> metrics = new LinkedHashMap<>();
			
			for(ChangeMetric cm : c.getChangeMetrics()) {
				MetricHolder mh = new MetricHolder();
				mh.changeMetric = cm;
				metrics.put(cm.getFile(), mh);
			}
			
			for(SzzMetric szz : c.getSzzMetrics()) {
				MetricHolder mh = metrics.get(szz.getFile());
				if(mh == null) {
					mh = new MetricHolder();
					metrics.put(szz.getFile(), mh);
				}
				mh.szzMetric = szz;
			}
			
			for(SourceMetric sm : c.getSourceMetrics()) {
				MetricHolder mh = metrics.get(sm.getFile());
				if(mh == null) {
					mh = new MetricHolder();
					metrics.put(sm.getFile(), mh);
				}
				mh.sourceMetric = sm;
			}
			
			Map<String, MetricHolder> metrics_sorted = metrics.entrySet().stream()
	                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
			
			

			for(Entry<String, MetricHolder> entry : metrics_sorted.entrySet()) {
				ChangeMetric cm = entry.getValue().changeMetric;
				SzzMetric szz = entry.getValue().szzMetric;
				SourceMetric sm = entry.getValue().sourceMetric;
				
				String file = entry.getKey();
				
				String[] in = {
					file,
					c.getDate().toString(),
					c.getRef(),
					// metrics
					cm != null ? ""+cm.getRevisions() : "0",
					cm != null ? ""+cm.getRefactorings() : "0",
					cm != null ? ""+cm.getBugfixes() : "0",
					cm != null ? ""+cm.getAuthors() : "0",
					cm != null ? ""+cm.getLocAdded() : "0",
					cm != null ? ""+cm.getMaxLocAdded() : "0",
					cm != null ? ""+cm.getAvgLocAdded() : "0",
					cm != null ? ""+cm.getLocRemoved() : "0",
					cm != null ? ""+cm.getMaxLocRemoved() : "0",
					cm != null ? ""+cm.getAvgLocRemoved() : "0",
					cm != null ? ""+cm.getCodeChurn() : "0",
					cm != null ? ""+cm.getMaxCodeChurn() : "0",
					cm != null ? ""+cm.getAvgCodeChurn() : "0",
					cm != null ? ""+cm.getMaxChangeset() : "0",
					cm != null ? ""+cm.getAvgChangeset() : "0",
					cm != null ? ""+cm.getAge() : "0",
					cm != null ? ""+cm.getWeightedAge() : "0",
					szz != null ? ""+szz.getBugs() : "0",
					sm != null ? sm.getClassName() : "",
					sm != null ? sm.getType() : "",
					sm != null ? ""+sm.getCbo() : "0",
					sm != null ? ""+sm.getWmc() : "0",
					sm != null ? ""+sm.getDit() : "0",
					sm != null ? ""+sm.getNoc() : "0",
					sm != null ? ""+sm.getRfc() : "0",
					sm != null ? ""+sm.getLcom() : "0",
					sm != null ? ""+sm.getNom() : "0",
					sm != null ? ""+sm.getNopm() : "0",
					sm != null ? ""+sm.getNosm() : "0",
					sm != null ? ""+sm.getNof() : "0",
					sm != null ? ""+sm.getNopf() : "0",
					sm != null ? ""+sm.getNosf() : "0",
					sm != null ? ""+sm.getNosi() : "0",
					sm != null ? ""+sm.getLoc() : "0",
					sm != null ? ""+sm.getNocb() : "0",
					sm != null ? ""+sm.getNonc() : "0",
					sm != null ? ""+sm.getNona() : "0",
					sm != null ? ""+sm.getNomwmop() : "0",
					sm == null ? "yes" : "no"
				};
				
				csv.writeLine(writer,  Arrays.asList(in));
			}
		}
		
		byte[] documentBody = writer.toString().getBytes();
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.set(HttpHeaders.CONTENT_DISPOSITION,
		              "attachment; filename=" + fileName.replace(" ", "_"));
		header.setContentLength(documentBody.length);
		
		return new HttpEntity<byte[]>(documentBody, header);	
	}
	
	private List<ChangeMetric> getChangeMetrics(Project project, String path, String ref, int timeWindow, boolean excludeBigCommits) {
		List<ChangeMetric> result = new ArrayList<>();
		
		ChangeMetrics cm = new ChangeMetrics(Paths.get(path));
		
		// cm.setThreads(20);		
		
		// get all "BUG"-FIX issues and add them to the bug repository
		CMBugRepository bugRepo = new CMBugRepository();
        
		List<CommitIssue> issues = commitIssueService.findAllByProjectAndType(project, CommitIssue.Type.BUG);
		
		Set<String> commits = new HashSet<>();
		for(CommitIssue i : issues) {
			for(Commit c : i.getCommits()) {
				commits.add(c.getRef());
			}
		}
		
		if(commits.size() == 0) commits = null;
		bugRepo.setBugCommits(commits);
        cm.setBugRepository(bugRepo);
        
        // range to only 1 commit!
        cm.setRange(ref, ref);
        
        if(excludeBigCommits) cm.excludeCommits( project.getBigCommits().stream().map(b -> b.getCommit().getRef()).collect(Collectors.toList()) );
		
		Map<String, CommitRange> list = cm.generateCommitListWithWeeks(timeWindow);

		for(Entry<String, CommitRange> e : list.entrySet()) {
			
        	String _ref = e.getKey();
        	
        	Commit commit = commitService.getCommitByProjectAndRef(project, _ref);
        	
        	if(commit == null) continue;
        	
        	cm.setRange(e.getValue());
        	CMRepository results = cm.analyze();
        	
			for(CMFile f : results.all()) {
				ChangeMetric c = new ChangeMetric(f);
				c.setCommit(commit);
				result.add(c);
			}
			
			break;

        }
		return result;
	}
	
	private List<SourceMetric> getSourceMetrics(Project project, String path, String ref) {
		List<SourceMetric> result = new ArrayList<>();
		
		GitRepository repo = new GitRepository(project, false);
		path = repo.getRepositoryPath().replace("\\.git",  "").replace("/.git", "");
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		
		SourceMetrics sourceMetrics = new SourceMetrics(Paths.get(path));
		
		// only 1 commit!
		
        SMRepository results = sourceMetrics.analyze(Arrays.asList(ref));
        
        for(SMCommit c : results.all()) {
        	
        	Commit commit = commitService.getCommitByProjectAndRef(project, c.getHash());
        	
        	if(commit == null) continue;
        	
        	for(SMFile f : c.getFiles().values()) {
        		SourceMetric sm = new SourceMetric(f);
        		sm.setCommit(commit);    		
        		result.add(sm);
        	}
        }
        return result;
	}
	
	/*private void analyzeBigCommits(Project project) {
		
		List<CommitIssue.Type> typeSet = new ArrayList<>();
	
		// init type set to analyze
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
	
		CommitAnalyzer ca = new CommitAnalyzer(project, new HashSet<CommitIssue.Type>(typeSet));
		ca.setCommitService(commitService);
		ca.load();
		ca.analyze();
		
		List<String> commitRefs = new ArrayList<>();
		for(BigCommit b : project.getBigCommits()) {
			commitRefs.add(b.getCommit().getRef());
		}
		
		List<BigCommit> toAdd = new ArrayList<>();
		
		for(Entry<CommitIssue.Type, List<Commit>> e : ca.getPossibleBigCommits().entrySet()) {
			for(Commit c : e.getValue()) {
				if(!commitRefs.contains(c.getRef())) {
					BigCommit b = new BigCommit();
					b.setCommit(c);
					b.setIssueType(e.getKey());
					toAdd.add(b);
				}
			}
		}
		
		project.addBigCommits(toAdd);
		projectService.update(project);
	}*/
}