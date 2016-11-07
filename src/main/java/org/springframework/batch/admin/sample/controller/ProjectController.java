package org.springframework.batch.admin.sample.controller;

import java.util.List;

import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	public ProjectController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("")
	public ModelAndView index() {
		List<Project> p = projectService.listAll();
		return new ModelAndView("projects/index", "projects", p);
	}

}