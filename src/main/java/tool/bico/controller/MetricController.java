package tool.bico.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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

import tool.bico.controller.form.ChangeMetricFormData;
import tool.bico.job.JobCreator;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.SzzMetric;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.model.service.SzzMetricService;
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

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		List<ChangeMetric> changeMetrics = changeMetricService.getProjectChangeMetrics(project);
		List<SzzMetric> szzMetrics = szzMetricService.getProjectSzzMetrics(project);
		
		model.addAttribute("project", project);
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("szzMetrics", szzMetrics);
		
		return new ModelAndView("projects/metrics/index", model.asMap());
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public HttpEntity<byte[]> download(@PathVariable("id") Long id) throws IOException {

		//byte[] documentBody = this.pdfFramework.createPdf(filename);
		
		Project project = projectService.findById(id);
		Set<Commit> commits = project.getCommits();
		
		String fileName = project.getName()+"_metrics.csv";
		
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
			"# SZZ Bugs"
		};
		
		
		csv.writeLine(writer, Arrays.asList(csvHeader));
		
		for(Commit c : commits) {
			
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
			
			Map<String, MetricHolder> metrics_sorted = metrics.entrySet().stream()
	                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
			
			

			for(Entry<String, MetricHolder> entry : metrics_sorted.entrySet()) {
				ChangeMetric cm = entry.getValue().changeMetric;
				SzzMetric szz = entry.getValue().szzMetric;
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
					szz != null ? ""+szz.getBugs() : "0"					
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
	
	class MetricHolder {
		SzzMetric szzMetric;
		ChangeMetric changeMetric;
	}
}
