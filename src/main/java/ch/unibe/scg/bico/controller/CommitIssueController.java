package ch.unibe.scg.bico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.CommitIssueService;
import ch.unibe.scg.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects/{id}/issues")
public class CommitIssueController {

	@Autowired
	private CommitIssueService commitIssueService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		List<CommitIssue> issues = commitIssueService.findAllByProject(project);
		return new ModelAndView("projects/issues/index", "issues", issues);
	}
}
