package ch.unibe.scg.bico.controller;

import java.util.List;

import javax.validation.Valid;

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

import ch.unibe.scg.bico.job.JobCreator;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	@Lazy
	private JobCreator jobCreator;
	
	public ProjectController() {
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
			redirect.addFlashAttribute("globalErrorMessage", "Project does not exist");
			return new ModelAndView("redirect:/projects");
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
	public ModelAndView delete(@PathVariable("id") Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		if(project == null) {
			redirect.addFlashAttribute("globalErrorMessage", "Project does not exist");
			return new ModelAndView("redirect:/projects");
		}
		
		jobCreator.removeJob(project);
		projectService.delete(project);
		redirect.addFlashAttribute("globalMessage", "Successfully deleted project");
		return new ModelAndView("redirect:/projects");
	}

	@RequestMapping(method = RequestMethod.GET, value = "modify/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		if(project == null) {
			redirect.addFlashAttribute("globalErrorMessage", "Project does not exist");
			return new ModelAndView("redirect:/projects");
		}
		
		return new ModelAndView("projects/edit_form", "project", project);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "modify/{id}")	
	public ModelAndView modifyFormUpdate(@ModelAttribute Project project, BindingResult result,
			RedirectAttributes redirect, @PathVariable("id") Long id) {
		if(result.hasErrors()) {
			//return new ModelAndView("projects/edit_form", "formErrors", result.getAllErrors());
			return new ModelAndView("projects/edit_form", "project", project);
		}
		
		Project dbProject = projectService.findById(id);
		String oldName = new String(dbProject.getName());
		
		dbProject.setBranch(project.getBranch());
		dbProject.setIssueTrackerUrlPattern(project.getIssueTrackerUrlPattern());
		dbProject.setName(project.getName());
		dbProject.setType(project.getType());
		dbProject.setUrl(project.getUrl());
		
		projectService.update(dbProject);
		jobCreator.removeJob(id, oldName);
		jobCreator.createJob(dbProject);
		
		redirect.addFlashAttribute("globalMessage", "Successfully updated project");
		return new ModelAndView("redirect:/projects/{project.id}", "project.id", dbProject.getId());
	}

}