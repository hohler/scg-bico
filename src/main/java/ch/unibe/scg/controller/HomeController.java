package ch.unibe.scg.controller;

//import org.springframework.web.bind.annotation.RestController;
/*import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@RequestMapping("/")
    public String index(Model model) {
		model.addAttribute("name", "test");
        return "home";
    }
	
}