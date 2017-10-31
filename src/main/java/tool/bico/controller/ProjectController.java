package tool.bico.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tool.bico.job.JobCreator;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.service.ProjectService;
import tool.bico.utils.CSVUtils;

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
	public ModelAndView view(Model model, @PathVariable("id") Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		if(project == null) {
			redirect.addFlashAttribute("globalErrorMessage", "Project does not exist");
			return new ModelAndView("redirect:/projects");
		}
		
		int commitAmountWithoutIssue = 0;
		int commitAmountWithIssues = 0;
		for(Commit c : project.getCommits()) {
			if(c.getCommitIssues().size() == 0) commitAmountWithoutIssue++;
			else commitAmountWithIssues++;
		}
		
		model.addAttribute("project", project);
		model.addAttribute("commitAmountWithoutIssue", commitAmountWithoutIssue);
		model.addAttribute("commitAmountWithIssues", commitAmountWithIssues);
		return new ModelAndView("projects/view", model.asMap());
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
	
	
	@RequestMapping(method = RequestMethod.GET, value = "{id}/export")
	public HttpEntity<byte[]> downloadCommitAnalysis(RedirectAttributes redirect, @PathVariable("id") Long id) throws IOException {

		Project project = projectService.findById(id);
		Set<Commit> commits = project.getCommits();
		
		String fileName = project.getName()+".csv";
		
		StringWriter writer = new StringWriter();
		CSVUtils csv = new CSVUtils(';');
		
		String[] csvHeader = {
			"Hash",
			"parentCommit",
			"Issues",
			"Additions",
			"Deletions",
			"isMergeCommit",
			"date",
			"commitMessageFirstLine"
		};
		
		csv.writeLine(writer, Arrays.asList(csvHeader));
		
		for(Commit c : commits) {
			
			String[] in = {
				c.getRef(),
				c.getParentCommit() != null ? c.getParentCommit().getRef() : "",
				""+ c.getCommitIssues().size(),
				""+ c.getAdditions(),
				""+ c.getDeletions(),
				c.isMergeCommit() ? "yes" : "no",
				c.getDate().toString(),
				c.firstLineOfMessage()
			};
				
			//csv.writeLine(writer, Arrays.asList(in), ',', '\'');
			csv.writeLine(writer, Arrays.asList(in));
		}
		
		byte[] documentBody = writer.toString().getBytes();
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.set(HttpHeaders.CONTENT_DISPOSITION,
		              "attachment; filename=" + fileName.replace(" ", "_"));
		header.setContentLength(documentBody.length);
		
		return new HttpEntity<byte[]>(documentBody, header);	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{id}/issue_categorization")
	public ModelAndView modifyForm(Model model, @PathVariable("id") Long id, RedirectAttributes redirect) {
		Project project = projectService.findById(id);
		
		if(project == null) {
			redirect.addFlashAttribute("globalErrorMessage", "Project does not exist");
			return new ModelAndView("redirect:/projects");
		}
		
		Set<Commit> commits = project.getCommits();
		
		model.addAttribute("project", project);
		model.addAttribute("commits", commits);
		
		return new ModelAndView("projects/issue_categorization", model.asMap());
	}

}