package org.springframework.batch.admin.sample.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@RequestMapping(method = RequestMethod.GET, name = "create")
	public String createForm(@ModelAttribute Project project) {
		return "project/create_form";
	}

	@RequestMapping(method = RequestMethod.POST, name = "create")
	public ModelAndView create(@Valid Project project, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("projects/create_form", "formErrors", result.getAllErrors());
		}
		projectService.add(project);
		redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
		return new ModelAndView("redirect:/projects/{project.id}", "project.id", project.getId());
	}

}