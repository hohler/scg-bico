package org.springframework.batch.admin.sample.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.batch.admin.sample.job.Job2Configuration;
import org.springframework.batch.admin.sample.job.JobCreator;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.ProjectService;
import org.springframework.batch.admin.sample.model.ProjectServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	@Lazy
	private JobCreator jobCreator;
	
	public ProjectController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() {
		List<Project> p = projectService.listAll();
		return new ModelAndView("projects/index", "projects", p);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}")
	public ModelAndView view(@PathVariable("id") Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		if(project == null) {
			redirect.addFlashAttribute("globalMessage", "This entity does not exist!");
			return new ModelAndView("projects");
		}
		return new ModelAndView("projects/view", "project", project);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "create")
	public String createForm(@ModelAttribute Project project) {
		return "projects/create_form";
	}

	@RequestMapping(method = RequestMethod.POST, value = "create")
	public ModelAndView create(@Valid Project project, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("projects/create_form", "formErrors", result.getAllErrors());
		}
		projectService.add(project);
		
		jobCreator.createJob(project);
		
		redirect.addFlashAttribute("globalMessage", "Successfully created a new project");
		return new ModelAndView("redirect:/projects/{project.id}", "project.id", project.getId());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "delete/{id}")
	public ModelAndView delete(Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		projectService.delete(project);
		redirect.addFlashAttribute("globalMessage", "Successfully deleted project");
		return new ModelAndView("redirect:/projects");
	}

	@RequestMapping(method = RequestMethod.GET, value = "modify/{id}")
	public ModelAndView modifyForm(Long id) {
		Project project = projectService.findById(id);
		return new ModelAndView("projects/edit_form", "project", project);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "modify/{id}")
	public ModelAndView modifyFormUpdate(@Valid Project project, BindingResult result,
			RedirectAttributes redirect) {
		if(result.hasErrors()) {
			return new ModelAndView("projects/edit_form", "formErrors", result.getAllErrors());
		}
		projectService.update(project);
		redirect.addFlashAttribute("globalMessage", "Successfully updated project");
		return new ModelAndView("redirect:/projects/{project.id}", "project.id", project.getId());
	}

}