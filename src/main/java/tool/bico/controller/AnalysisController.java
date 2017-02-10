package tool.bico.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.analysis.CommitAnalyzer;
import tool.bico.analysis.ResultsContainer;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects/{pid}/analyze")
public class AnalysisController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CommitService commitService;
	
	private List<CommitIssue.Type> typeSet = new ArrayList<>();
	
	public AnalysisController() {
		// init type set to analyze
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(Model model, @PathVariable("pid") Long pid) {
		Project project = projectService.findById(pid);
		
		CommitAnalyzer ca = new CommitAnalyzer(project, new HashSet<CommitIssue.Type>(typeSet));
		ca.load();
		ca.analyze();
		Map<CommitIssue.Type, ResultsContainer> results = ca.getTypeResults();

		model.addAttribute("types", typeSet);
		model.addAttribute("results", results);
		model.addAttribute("project", project);
		
		return new ModelAndView("projects/analysis/view", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "bigcommits")
	public ModelAndView bigCommits(Model model, @PathVariable("pid") Long pid) {
		
		Project project = projectService.findById(pid);
		
		CommitAnalyzer ca = new CommitAnalyzer(project, new HashSet<CommitIssue.Type>(typeSet));
		ca.setCommitService(commitService);
		ca.load();
		ca.analyze();
		
		model.addAttribute("commits", ca.getPossibleBigCommits());
		model.addAttribute("project", project);
		model.addAttribute("thresholds", ca.getThresholds());
		
		return new ModelAndView("projects/analysis/bigcommit", model.asMap());
	}
}
