package nordgym.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactsController extends BaseController{

    @GetMapping("/contacts")
    public ModelAndView contacts (){
        return this.view("contacts");
    }
}
