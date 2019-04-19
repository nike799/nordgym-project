package com.nordgym.web.controllers;

import com.nordgym.domain.models.view.UserViewModel;
import com.nordgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView adminHome(ModelAndView modelAndView, Authentication authentication, HttpServletRequest request) {
        modelAndView.addObject("username", authentication.getName());
        modelAndView.addObject("authorities", authentication.getAuthorities());
        modelAndView.addObject("admins",this.userService.getAllAdmins());

        if (request.getSession().getAttribute("userViewModels") != null) {
            modelAndView.addObject("userViewModels", request.getSession().getAttribute("userViewModels"));
            request.getSession().removeAttribute("userViewModels");
        } else {
            List<UserViewModel> userViewModels = this.userService.getAllUserViewModels();
            modelAndView.addObject("userViewModels", userViewModels);
        }
        return this.view("admin-home", modelAndView);
    }

    @PostMapping(value = "/home", params = "criteria")
    @ResponseBody
    public ModelAndView searchUserById(@RequestParam("criteria") String criteria, HttpServletRequest request) {
        List<UserViewModel> userViewModels = this.userService.getSearchedUsers(criteria);
        request.getSession().setAttribute("userViewModels", userViewModels);
        return this.redirect("/home");
    }
}
