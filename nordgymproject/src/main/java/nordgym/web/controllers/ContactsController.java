package nordgym.web.controllers;

import nordgym.configuration.MailComponent;
import nordgym.domain.models.binding.ContactFormBindingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ContactsController extends BaseController {
    private final MailComponent mailComponent;

    @Autowired
    public ContactsController(MailComponent mailComponent) {
        this.mailComponent = mailComponent;
    }

    @GetMapping("/contacts")
    public ModelAndView contacts(@ModelAttribute ContactFormBindingModel contactFormBindingModel){
        return this.view("contacts");
    }

    @PostMapping("/contacts")
    public ModelAndView processContactForm(@Valid @ModelAttribute ContactFormBindingModel contactFormBindingModel, BindingResult bindingResult,ModelAndView modelAndView) {
        System.out.println();
        if (bindingResult.hasErrors()) {
            return this.view("contacts");
        }
        if (this.mailComponent.sendSimpleMail(contactFormBindingModel)) {
            modelAndView.addObject("classCss", "alert alert-success text-center");
            modelAndView.addObject("message", "Съобщението беше изпратено успешно!");
            return this.view("contacts", modelAndView);
        } else {
            modelAndView.addObject("classCss", "alert alert-danger text-center");
            modelAndView.addObject("message", "Възникна проблем при изпращане на съобщението. Моля, опитайте отново!");
            return this.view("contacts", modelAndView);
        }
    }
}
