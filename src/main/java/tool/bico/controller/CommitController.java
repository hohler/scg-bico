package tool.bico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.model.Commit;
import tool.bico.model.service.CommitService;

@Controller
@RequestMapping("/projects/{id}/commits")
public class CommitController {
	
	@Autowired
	private CommitService commitService;
	
	@RequestMapping(method = RequestMethod.GET, value="{cid}")
	public ModelAndView view(Model model, @PathVariable("id") Long id, @PathVariable("cid") Long cid) {
		Commit commit = commitService.findById(cid);
		model.addAttribute("commit", commit);
		model.addAttribute("newLineChar", "\n");
		
		return new ModelAndView("projects/commits/view", model.asMap());
	}
}
