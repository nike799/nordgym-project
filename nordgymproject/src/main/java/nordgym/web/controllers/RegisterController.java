package nordgym.web.controllers;

import nordgym.GlobalConstants;
import nordgym.domain.models.binding.TrainingProgramBindingModel;
import nordgym.domain.models.binding.UserRegisterBindingModel;
import nordgym.domain.models.service.TrainingProgramServiceModel;
import nordgym.domain.models.service.UserServiceModel;
import nordgym.service.TrainingProgramService;
import nordgym.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/register")
public class RegisterController extends BaseController {
    private final UserService userService;
    private final TrainingProgramService trainingProgramService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegisterController(UserService userService, TrainingProgramService trainingProgramService, ModelMapper modelMapper) {
        this.userService = userService;
        this.trainingProgramService = trainingProgramService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user-new")
    public ModelAndView userRegister(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel, ModelAndView modelAndView, Authentication authentication) {
        modelAndView.addObject("username", authentication.getName());
        return this.view("/register-user", modelAndView);
    }

    @PostMapping("/user-new")
    public ModelAndView userRegisterConfirm(@Valid @ModelAttribute UserRegisterBindingModel userRegisterBindingModel,
                                     BindingResult bindingResult, @RequestParam("profileImage") MultipartFile image) throws IOException {
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", userRegisterBindingModel.getConfirmPassword(), GlobalConstants.PASSWORDS_NOT_EQUALS);
            return this.view("register-user");
        }
        if (bindingResult.hasErrors()) {
            return this.view("register-user");
        }
        if (!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
            File dest = new File(GlobalConstants.IMAGES_PATH + image.getOriginalFilename());
            image.transferTo(dest);
            userRegisterBindingModel.setProfileImagePath(image.getOriginalFilename());
        } else {
            userRegisterBindingModel.setProfileImagePath("avatar.jpg");
        }
        this.userService.createUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class), userRegisterBindingModel.getSubscription());
        return this.redirect("/home");
    }

    @GetMapping("/training-program-new")
    public ModelAndView trainingProgramRegister(@ModelAttribute TrainingProgramBindingModel trainingProgramBindingModel, ModelAndView modelAndView, Authentication authentication) {
        modelAndView.addObject("username", authentication.getName());
        return this.view("training-program-register", modelAndView);
    }

    @PostMapping("/training-program-new")
    public ModelAndView trainingProgramRegisterConfirm(@Valid @ModelAttribute TrainingProgramBindingModel trainingProgramBindingModel,BindingResult bindingResult,@RequestParam("programImage") MultipartFile image) throws IOException {
        if (bindingResult.hasErrors()) {
            return this.view("training-program-register");
        }
        TrainingProgramServiceModel trainingProgramServiceModel = this.modelMapper.map(trainingProgramBindingModel,TrainingProgramServiceModel.class);
        Long id = this.trainingProgramService.registerTrainingProgram(trainingProgramServiceModel,"/images/" + image.getOriginalFilename());
        File dest = new File(GlobalConstants.IMAGES_PATH + image.getOriginalFilename());
        image.transferTo(dest);
        return this.redirect("/training-programs/"+id);
    }
}
