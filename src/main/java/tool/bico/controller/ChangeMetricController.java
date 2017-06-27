package tool.bico.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tool.bico.analysis.BigCommitAnalyzer;
import tool.bico.controller.form.ChangeMetricFormData;
import tool.bico.job.JobCreator;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.SourceMetric;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.utils.CSVUtils;


@Controller
@RequestMapping("/projects/{id}/metrics/changemetrics")
public class ChangeMetricController {

	@Autowired
	private ChangeMetricService changeMetricService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CommitService commitService;
	
	@Autowired
	@Lazy
	private JobCreator jobCreator;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		
		List<ChangeMetric> changeMetrics = changeMetricService.getProjectChangeMetrics(project);
		if(changeMetrics == null) changeMetrics = new ArrayList<>();
		
		Map<Commit, List<ChangeMetric>> cm = new HashMap<>();
		
		for(ChangeMetric c : changeMetrics) {
			List<ChangeMetric> list = cm.get(c.getCommit());
			if(list == null) {
				list = new ArrayList<ChangeMetric>();
				cm.put(c.getCommit(), list);
			}
			list.add(c);
		}
		
		Map<Commit, List<ChangeMetric>> cm_sorted = cm.entrySet().stream()
                .sorted((e1,e2) -> e1.getKey().getId().compareTo(e2.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		
		model.addAttribute("cm", cm_sorted);
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("project", project);
		
		boolean modified = false;
		
		if(project.getChangeMetricTimeWindow() == 0) {
			project.setChangeMetricTimeWindow(12);
			modified = true;
		}
		
		if(project.getChangeMetricEveryCommits() == 0) {
			project.setChangeMetricEveryCommits(100);
			modified = true;
		}
		
		if(modified) projectService.update(project);
		
		ChangeMetricFormData cmf = new ChangeMetricFormData();
		cmf.setTimeWindow(project.getChangeMetricTimeWindow());
		cmf.setEveryCommits(project.getChangeMetricEveryCommits());
		cmf.setExcludeBigCommits(project.getChangeMetricsExcludeBigCommits());
		
		model.addAttribute("cmf", cmf);

		return new ModelAndView("projects/metrics/changemetrics/index", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView changeMetricVariables(ChangeMetricFormData cmf, @PathVariable("id") Long id, BindingResult result, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		//project.setChangeMetricTimeRange()
		
		project.setChangeMetricTimeWindow(cmf.getTimeWindow());
		project.setChangeMetricEveryCommits(cmf.getEveryCommits());
		project.setChangeMetricsExcludeBigCommits(cmf.getExcludeBigCommits());
		
		if(cmf.getExcludeBigCommits()) {
			BigCommitAnalyzer.analyzeBigCommits(project, projectService, commitService);
		}
		
		this.projectService.update(project);
		
		this.jobCreator.removeMetricsJob(project);
		this.jobCreator.createMetricsJob(project);
		
		//System.err.println(cmf.getTimeRange());
		
		redirect.addFlashAttribute("globalMessage", "Successfully updated the variables");
		return new ModelAndView("redirect:/projects/{project.id}/metrics/changemetrics", "project.id", project.getId());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{cid}")
	public ModelAndView view(Model model, @PathVariable("id") Long id, @PathVariable("cid") Long cid) {
		Project project = projectService.findById(id);
		Commit commit = commitService.findById(cid);
		List <ChangeMetric> changeMetrics = changeMetricService.getChangeMetricsByCommit(commit);
		
		model.addAttribute("project", project);
		model.addAttribute("commit", commit);
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("newLineChar", "\n");
		
		return new ModelAndView("projects/metrics/changemetrics/view", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="file")
	public ModelAndView viewFileHistory(Model model, @PathVariable("id") Long id, @RequestParam("file") String file) {
		Project project = projectService.findById(id);
		List<ChangeMetric> changeMetrics = changeMetricService.findByFileAndProject(file, project);
		
		Commit commit = changeMetrics.get(0).getCommit();
		
		model.addAttribute("project", project);
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("commit", commit);
		model.addAttribute("newLineChar", "\n");
		
		return new ModelAndView("projects/metrics/changemetrics/file_view", model.asMap());
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadAll(@PathVariable("id") Long id) throws IOException {

		
		Project project = projectService.findById(id);
		//Set<Commit> commits = project.getCommits();
		
		List<ChangeMetric> changeMetrics = changeMetricService.getProjectChangeMetrics(project);
		
		return downloadChangeMetrics(project, changeMetrics, null);
	}
	
	@RequestMapping(value = "{cid}/download", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadSingle(@PathVariable("id") Long id, @PathVariable("cid") Long cid) throws IOException {

		
		Project project = projectService.findById(id);
		//Set<Commit> commits = project.getCommits();
		Commit commit = commitService.findById(cid);
		
		List<ChangeMetric> changeMetrics = changeMetricService.getChangeMetricsByCommit(commit);
		
		return downloadChangeMetrics(project, changeMetrics, commit.getRef());
	}
	
	
	private HttpEntity<byte[]> downloadChangeMetrics(Project project, List<ChangeMetric> changeMetrics, String ref) throws IOException {
		String fileName = project.getId()+"_"+project.getName()+"_"+(ref != null ? ref : "")+"changemetrics.csv";
		
		StringWriter writer = new StringWriter();
		CSVUtils csv = new CSVUtils(';');
		
		String[] csvHeader = {
			"Commit",
			"Date",
			"File",
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
		};
		
		
		csv.writeLine(writer, Arrays.asList(csvHeader));
			
		changeMetrics.sort( (u1, u2) -> { 
			int comp_c = u1.getCommit().getId().compareTo(u2.getCommit().getId());
			
			if(comp_c != 0) {
				return comp_c;
			} else {
				return u1.getFile().compareTo(u2.getFile());
			}
		});
		
		for(ChangeMetric cm : changeMetrics) {
			String[] in = {
				cm.getCommit().getRef(),
				cm.getCommit().getDate().toString(),
				cm.getFile(),
				// metrics
				""+cm.getRevisions(),
				""+cm.getRefactorings(),
				""+cm.getBugfixes(),
				""+cm.getAuthors(),
				""+cm.getLocAdded(),
				""+cm.getMaxLocAdded(),
				""+cm.getAvgLocAdded(),
				""+cm.getLocRemoved(),
				""+cm.getMaxLocRemoved(),
				""+cm.getAvgLocRemoved(),
				""+cm.getCodeChurn(),
				""+cm.getMaxCodeChurn(),
				""+cm.getAvgCodeChurn(),
				""+cm.getMaxChangeset(),
				""+cm.getAvgChangeset(),
				""+cm.getAge(),
				""+cm.getWeightedAge(),
			};
			
			csv.writeLine(writer,  Arrays.asList(in));
		}
		
		byte[] documentBody = writer.toString().getBytes();
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.set(HttpHeaders.CONTENT_DISPOSITION,
		              "attachment; filename=" + fileName.replace(" ", "_"));
		header.setContentLength(documentBody.length);
		
		return new HttpEntity<byte[]>(documentBody, header);
	}
	
}
