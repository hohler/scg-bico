package tool.bico.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.model.BigCommit;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.CommitIssueAnalysis;
import tool.bico.model.Project;
import tool.bico.model.service.BigCommitService;
import tool.bico.model.service.CommitIssueAnalysisService;
import tool.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects/{pid}/analyze")
public class AnalysisController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BigCommitService bigCommitService;
	
	@Autowired
	private CommitIssueAnalysisService commitIssueAnalysisService;
	
	private List<CommitIssue.Type> typeSet = new ArrayList<>();
	
	public AnalysisController() {
		// init type set to analyze
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
		typeSet.add(CommitIssue.Type.NA);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(Model model, @PathVariable("pid") Long pid) {
		Project project = projectService.findById(pid);

		List<CommitIssueAnalysis> cia = commitIssueAnalysisService.findAllByProject(project);
		Map<CommitIssue.Type, CommitIssueAnalysis> results = cia.stream().collect(Collectors.toMap(c -> c.getType(), c->c));
		
		model.addAttribute("types", typeSet);
		model.addAttribute("results", results);
		model.addAttribute("project", project);
		
		return new ModelAndView("projects/analysis/view", model.asMap());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "bigcommits")
	public ModelAndView bigCommits(Model model, @PathVariable("pid") Long pid) {
		
		Project project = projectService.findById(pid);
		
		List<BigCommit> list = bigCommitService.getProjectBigCommits(project);
		
		Map<CommitIssue.Type, List<Commit>> bigCommits = new HashMap<>();
		for(BigCommit b : list) {
			if(b.getIssueType() == null) continue;
			if(bigCommits.get(b.getIssueType()) == null) bigCommits.put(b.getIssueType(), new ArrayList<Commit>());
			List<Commit> l = bigCommits.get(b.getIssueType());
			l.add(b.getCommit());
		}
		
		Map<CommitIssue.Type, Map<String, Integer>> thresholdContainer = new HashMap<>();
		
		List<CommitIssueAnalysis> cia = commitIssueAnalysisService.findAllByProject(project);
		for(CommitIssueAnalysis c : cia) {

			Map<String, Integer> thresholds = new HashMap<>();

			thresholds.put("firstQuartileFilesChanged", c.getFirstQuartileFilesChanged());
			thresholds.put("thirdQuartileFilesChanged", c.getThirdQuartileFilesChanged());
			thresholds.put("filesChangedThreshold", c.getFilesChangedThreshold());
			
			thresholds.put("firstQuartileAdditions",  c.getFirstQuartileAdditions());
			thresholds.put("thirdQuartileAdditions", c.getThirdQuartileAdditions());
			thresholds.put("additionsThreshold", c.getAdditionsThreshold());
			
			thresholdContainer.put(c.getType(), thresholds);

		}
			
		model.addAttribute("commits", bigCommits);
		model.addAttribute("project", project);
		model.addAttribute("thresholds", thresholdContainer);
		
		return new ModelAndView("projects/analysis/bigcommit", model.asMap());
	}
}
