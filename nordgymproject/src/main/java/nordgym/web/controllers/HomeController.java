package nordgym.web.controllers;

import nordgym.domain.models.view.UserViewModel;
import nordgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
            return this.redirect("/admin/home");
        }
        UserViewModel userViewModel = this.userService.createUserViewModel(this.userService.getUserByUsername(authentication.getName()));
        modelAndView.addObject("username", authentication.getName());
        modelAndView.addObject("userViewModel", userViewModel);
        return this.redirect("/user-profile/" + userViewModel.getId());
    }
}
