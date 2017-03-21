package tool.bico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.model.ChangeMetric;
import tool.bico.model.Project;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.ProjectService;


@Controller
@RequestMapping("/projects/{id}/metrics/changemetrics")
public class ChangeMetricController {

	@Autowired
	private ChangeMetricService changeMetricService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		
		List<ChangeMetric> changeMetrics = changeMetricService.getProjectChangeMetrics(project);
		
		model.addAttribute("changeMetrics", changeMetrics);
		model.addAttribute("project", project);

		return new ModelAndView("projects/metrics/changemetrics/index", model.asMap());
	}
}
