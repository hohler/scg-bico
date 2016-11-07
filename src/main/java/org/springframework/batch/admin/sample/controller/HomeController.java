package org.springframework.batch.admin.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String greeting(Model model) {
    	// model.addAttribute("name", name);
        return "home";
    }

}
