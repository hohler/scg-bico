package org.springframework.batch.admin.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private ApplicationContext applicationContext;
    
    @RequestMapping("/home")
    public String greeting(Model model) {
    	// model.addAttribute("name", name);
        return "home";
    }

    @RequestMapping("/beans")
    public String beans(Model model) {

        String[] beanNames = applicationContext.getBeanDefinitionNames();

        for (String beanName : beanNames) {

            System.out.println(beanName + " : " + applicationContext.getBean(beanName).getClass().toString());
        }

        return "home";
    }
}
