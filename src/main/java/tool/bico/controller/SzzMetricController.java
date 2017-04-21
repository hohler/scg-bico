package tool.bico.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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


@Controller
@RequestMapping("/projects/{id}/metrics/szz")
public class SzzMetricController {

	@Autowired
	private SzzMetricService szzMetricService;
	
	@Autowired
	private ProjectService projectService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		
		List<SzzMetric> szzMetrics = szzMetricService.getProjectSzzMetrics(project);
		if(szzMetrics == null) szzMetrics = new ArrayList<>();
		
		Map<String, FileInfo> fileInfos = new HashMap<>();
		Map<FileInfo, List<SzzMetric>> szz = new HashMap<>();
		for(SzzMetric s : szzMetrics) {
			
			FileInfo fInfo = fileInfos.get(s.getFile());
			if(fInfo == null) {
				fInfo = new FileInfo(s.getFile());
				fileInfos.put(s.getFile(), fInfo);
			}
			
			List<SzzMetric> list = szz.get(fInfo);
			if(list == null) {
				list = new ArrayList<SzzMetric>();
				szz.put(fInfo, list);
			}
			list.add(s);
			fInfo.bugfixes += s.isBugfix() ? 1 : 0;
			fInfo.bugs += s.getBugs();
		}
		
		
		Map<FileInfo, List<SzzMetric>> szz_sorted = szz.entrySet().stream()
                .sorted((e1,e2) -> e1.getKey().file.compareTo(e2.getKey().file))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		
		
		model.addAttribute("szz", szz_sorted);
		model.addAttribute("szzMetrics", szzMetrics);
		model.addAttribute("project", project);
		
	
		return new ModelAndView("projects/metrics/szz/index", model.asMap());
	}
	
	
	/*@RequestMapping(method = RequestMethod.GET, value="{cid}")
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
	*/
	@RequestMapping(method = RequestMethod.GET, value="file")
	public ModelAndView viewFileHistory(Model model, @PathVariable("id") Long id, @RequestParam("file") String file) {
		Project project = projectService.findById(id);
		List<SzzMetric> szzMetrics = szzMetricService.findByFileAndProject(file, project);
		
		String fileName = szzMetrics.get(0).getFile();
		
		List<SzzMetric> szz_sorted = szzMetrics.stream()
                .sorted((e1,e2) -> e1.getCommit().getId().compareTo(e2.getCommit().getId()))
                .collect(Collectors.toList());
		
		
		model.addAttribute("project", project);
		model.addAttribute("szzMetrics", szz_sorted);
		model.addAttribute("file", fileName);
		model.addAttribute("newLineChar", "\n");
		
		return new ModelAndView("projects/metrics/szz/file_view", model.asMap());
	}
	
	class FileInfo {
		String file;
		int bugfixes;
		int bugs;
		
		public FileInfo(String file) {
			this.file = file;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof FileInfo)) return false;
			FileInfo other = (FileInfo) obj;
			return other.file.equals(file);
		}
		
		public String getFile() { return file; }
		public int getBugfixes() { return bugfixes; }
		public int getBugs() { return bugs; }
		
	}
	
}
