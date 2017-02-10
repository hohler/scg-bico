package tool.bico.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects/{id}/issues")
public class CommitIssueController {

	@Autowired
	private CommitIssueService commitIssueService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Model model, @PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		List<CommitIssue> issues = commitIssueService.findAllByProject(project);
		
		HashMap<CommitIssue.Type, List<CommitIssue>> issuesMapped = new HashMap<>();
		
		//init issuesMapped
		for(CommitIssue.Type t : CommitIssue.Type.values()) {
			issuesMapped.put(t, new ArrayList<CommitIssue>());
		}
		
		for(CommitIssue i : issues) {
			CommitIssue.Type type = i.getType();
			List<CommitIssue> list = issuesMapped.get(type);
			list.add(i);
		}
		
		Iterator<CommitIssue.Type> it = issuesMapped.keySet().iterator();
		while(it.hasNext()) {
			CommitIssue.Type t = it.next();
			if(issuesMapped.get(t).isEmpty()) it.remove();
		}
		
		model.addAttribute("project", project);
		model.addAttribute("issues", issues);
		model.addAttribute("issuesMapped", issuesMapped);
		return new ModelAndView("projects/issues/index", model.asMap());
	}
}
