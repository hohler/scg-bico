package org.springframework.batch.admin.sample.controller;

import org.springframework.batch.admin.sample.job.JobCreator;
import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects/{id}")
public class CommitController {
	
	@Autowired
	private CommitService commitService;
	
	@Autowired
	@Lazy
	private JobCreator jobCreator;
	
	@RequestMapping(method = RequestMethod.GET, value="commits/{cid}")
	public ModelAndView viewCommit(@PathVariable("id") Long id, @PathVariable("cid") Long cid) {
		Commit commit = commitService.findById(cid);
		return new ModelAndView("projects/commits/view", "commit", commit);
	}
}
