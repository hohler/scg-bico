package ch.unibe.scg.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RestController;
/*import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.unibe.scg.controller.service.ProjectService;
import ch.unibe.scg.model.Project;
//import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class IndexController {
	
	private ProjectService projectService;

	@Autowired
    public void setProductService(ProjectService projectService) {
        this.projectService = projectService;
    }
	
	@RequestMapping("/")
    public String index(Model model) {
		model.addAttribute("name", "test");
		
		Project p = new Project(Project.Type.GIT);
		p.setName("blah");
		
		projectService.saveProject(p);
		
		
        return "index";
    }
	
}