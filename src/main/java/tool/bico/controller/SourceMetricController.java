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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tool.bico.controller.form.SourceMetricFormData;
import tool.bico.job.JobCreator;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.SourceMetric;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.model.service.SourceMetricService;
import tool.bico.utils.CSVUtils;


@Controller
@RequestMapping("/projects/{id}/metrics/sourcemetrics")
public class SourceMetricController {

	@Autowired
	private SourceMetricService sourceMetricService;
	
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
		
		List<SourceMetric> sourceMetrics = sourceMetricService.getProjectSourceMetrics(project);
		if(sourceMetrics == null) sourceMetrics = new ArrayList<>();
		
		Map<Commit, List<SourceMetric>> sm = new HashMap<>();
		
		for(SourceMetric s : sourceMetrics) {
			List<SourceMetric> list = sm.get(s.getCommit());
			if(list == null) {
				list = new ArrayList<SourceMetric>();
				sm.put(s.getCommit(), list);
			}
			list.add(s);
		}
		
		Map<Commit, List<SourceMetric>> sm_sorted = sm.entrySet().stream()
                .sorted((e1,e2) -> e1.getKey().getId().compareTo(e2.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		
		model.addAttribute("sm", sm_sorted);
		model.addAttribute("sourceMetrics", sourceMetrics);
		model.addAttribute("project", project);
		
		
		
		SourceMetricFormData smf = new SourceMetricFormData();
		smf.setEveryCommits(project.getSourceMetricEveryCommits());
		smf.setExcludeBigCommits(project.getSourceMetricsExcludeBigCommits());
		
		model.addAttribute("smf", smf);

		return new ModelAndView("projects/metrics/sourcemetrics/index", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView sourceMetricVariables(SourceMetricFormData smf, @PathVariable("id") Long id, BindingResult result, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		project.setSourceMetricEveryCommits(smf.getEveryCommits());
		
		project.setSourceMetricsExcludeBigCommits(smf.getExcludeBigCommits());
		
		this.projectService.update(project);
		
		this.jobCreator.removeMetricsJob(project);
		this.jobCreator.createMetricsJob(project);
		
		redirect.addFlashAttribute("globalMessage", "Successfully updated the variables");
		return new ModelAndView("redirect:/projects/{project.id}/metrics/sourcemetrics", "project.id", project.getId());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{cid}")
	public ModelAndView view(Model model, @PathVariable("id") Long id, @PathVariable("cid") Long cid) {
		Project project = projectService.findById(id);
		Commit commit = commitService.findById(cid);
		List <SourceMetric> sourceMetrics = sourceMetricService.getSourceMetricsByCommit(commit);
		
		model.addAttribute("project", project);
		model.addAttribute("commit", commit);
		model.addAttribute("sourceMetrics", sourceMetrics);
		model.addAttribute("newLineChar", "\n");
		
		return new ModelAndView("projects/metrics/sourcemetrics/view", model.asMap());
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadAll(@PathVariable("id") Long id) throws IOException {

		
		Project project = projectService.findById(id);
		//Set<Commit> commits = project.getCommits();
		
		List<SourceMetric> sourceMetrics = sourceMetricService.getProjectSourceMetrics(project);
		
		return downloadSourceMetrics(project, sourceMetrics);
	}
	
	@RequestMapping(value = "{cid}/download", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadSingle(@PathVariable("id") Long id, @PathVariable("cid") Long cid) throws IOException {

		
		Project project = projectService.findById(id);
		//Set<Commit> commits = project.getCommits();
		Commit commit = commitService.findById(cid);
		
		List<SourceMetric> sourceMetrics = sourceMetricService.getSourceMetricsByCommit(commit);
		
		return downloadSourceMetrics(project, sourceMetrics);
	}
	
	
	private HttpEntity<byte[]> downloadSourceMetrics(Project project, List<SourceMetric> sourceMetrics) throws IOException {
		String fileName = project.getId()+"_"+project.getName()+"_sourcemetrics.csv";
		
		StringWriter writer = new StringWriter();
		CSVUtils csv = new CSVUtils(';');
		
		String[] csvHeader = {
			"Commit",
			"Date",
			"File",
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
			"NOMWMOP"
		};
		
		
		csv.writeLine(writer, Arrays.asList(csvHeader));
			
		sourceMetrics.sort( (u1, u2) -> { 
			int comp_c = u1.getCommit().getId().compareTo(u2.getCommit().getId());
			
			if(comp_c != 0) {
				return comp_c;
			} else {
				return u1.getFile().compareTo(u2.getFile());
			}
		});
		
		for(SourceMetric s : sourceMetrics) {
			String[] in = {
				s.getCommit().getRef(),
				s.getCommit().getDate().toString(),
				s.getFile(),
				s.getClassName(),
				s.getType(),
				""+s.getCbo(),
				""+s.getWmc(),
				""+s.getDit(),
				""+s.getNoc(),
				""+s.getRfc(),
				""+s.getLcom(),
				""+s.getNom(),
				""+s.getNopm(),
				""+s.getNosm(),
				""+s.getNof(),
				""+s.getNopf(),
				""+s.getNosf(),
				""+s.getNosi(),
				""+s.getLoc(),
				""+s.getNocb(),
				""+s.getNonc(),
				""+s.getNona(),
				""+s.getNomwmop()
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
