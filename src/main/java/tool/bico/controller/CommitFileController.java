package tool.bico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tool.bico.model.CommitFile;
import tool.bico.model.service.CommitFileService;

@Controller
@RequestMapping("/projects/{id}/commits/{cid}/files")
public class CommitFileController {
	
	@Autowired
	private CommitFileService commitFileService;
	
	@RequestMapping(method = RequestMethod.GET, value="{fid}")
	public ModelAndView view(@PathVariable("id") Long id, @PathVariable("cid") Long cid, @PathVariable("fid") Long fid) {
		CommitFile commitFile = commitFileService.findById(fid);

		return new ModelAndView("projects/commits/files/view", "commitFile", commitFile);
	}
}
