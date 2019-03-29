package nordgym.web.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

@Controller
public class UserLoginController extends BaseController {

    @GetMapping("/login")
    public ModelAndView login(Principal principal) {
        return principal == null ? this.view("/login") : this.redirect("/home");
    }

}


