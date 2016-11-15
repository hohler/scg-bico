package ch.unibe.scg.bico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.service.CommitService;

@Controller
@RequestMapping("/projects/{id}/commits")
public class CommitController {
	
	@Autowired
	private CommitService commitService;
	
	@RequestMapping(method = RequestMethod.GET, value="{cid}")
	public ModelAndView view(@PathVariable("id") Long id, @PathVariable("cid") Long cid) {
		Commit commit = commitService.findById(cid);
		return new ModelAndView("projects/commits/view", "commit", commit);
	}
}
