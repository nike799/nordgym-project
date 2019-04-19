package com.nordgym.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServicesController extends BaseController{
    @GetMapping("/services")
    public ModelAndView services(){
        return this.view("services");
    }
}
