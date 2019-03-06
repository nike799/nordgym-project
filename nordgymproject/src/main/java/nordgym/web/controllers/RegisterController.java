package nordgym.web.controllers;

import nordgym.domain.models.binding.UserRegisterBindingModel;
import nordgym.domain.models.service.UserServiceModel;
import nordgym.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Controller
public class RegisterController extends BaseController {
    private final static String IMAGE_PATH = "C:\\Users\\Nike\\Desktop\\Softuni Projects\\nordgymproject\\src\\main\\resources\\static\\images\\";
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public RegisterController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ModelAndView register(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel,ModelAndView modelAndView, Authentication authentication) {
        modelAndView.addObject("username", authentication.getName());
        return this.view("/register",modelAndView);
    }

    @PostMapping("/register")
    public @ResponseBody
    ModelAndView registerConfirm(@Valid @ModelAttribute UserRegisterBindingModel userRegisterBindingModel,
                                 BindingResult bindingResult, @RequestParam("profileImage") MultipartFile image) throws IOException {
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", userRegisterBindingModel.getConfirmPassword(), "Confirmed password is not equal to password!");
            return this.view("register");
        }
        if (bindingResult.hasErrors()) {
            return this.redirect("/register");
        }
        if (!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
            File dest = new File(IMAGE_PATH + image.getOriginalFilename());
            image.transferTo(dest);
            userRegisterBindingModel.setProfileImagePath(image.getOriginalFilename());
        }else {
            userRegisterBindingModel.setProfileImagePath("avatar.jpg");
        }
        this.userService.createUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class),userRegisterBindingModel.getSubscription());
        return this.redirect("/home");
    }

}
