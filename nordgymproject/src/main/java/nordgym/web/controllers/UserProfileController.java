package nordgym.web.controllers;

import nordgym.constants.GlobalConstants;
import nordgym.domain.models.binding.UserUpdateBindingModel;
import nordgym.domain.models.view.UserViewModel;
import nordgym.error.ResourceNotFoundException;
import nordgym.service.UserEntryService;
import nordgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/user-profile", "/user-profile/{id}"})
public class UserProfileController extends BaseController {
    private final UserService userService;
    private final UserEntryService userEntryService;

    @Autowired
    public UserProfileController(UserService userService, UserEntryService userEntryService) {
        this.userService = userService;
        this.userEntryService = userEntryService;
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{id}")
    public ModelAndView getUserProfile(@PathVariable String id, ModelAndView modelAndView, Authentication authentication) {

        UserViewModel userViewModel = this.userService.createUserViewModel(this.userService.getUserById(id));
        UserUpdateBindingModel userUpdateBindingModel = this.userService.getUserUpdateBindingModelByUserId(id);
        modelAndView.addObject("username", authentication.getName());
        modelAndView.addObject("userViewModel", userViewModel);
        modelAndView.addObject("userId", userViewModel.getId());
        modelAndView.addObject("userUpdateBindingModel", userUpdateBindingModel);
        return this.view("user-profile", modelAndView);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN','USER')")
    @PostMapping(value = "/{id}",params = "edit")
    public ModelAndView editUser(@ModelAttribute UserUpdateBindingModel userUpdateBindingModel, @PathVariable String id, BindingResult bindingResult) throws NoSuchFieldException, IllegalAccessException {
        if (!userUpdateBindingModel.getPassword().equals(userUpdateBindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", userUpdateBindingModel.getConfirmPassword(), GlobalConstants.PASSWORDS_NOT_EQUALS);
        }
        if (bindingResult.hasErrors()) {
            return this.redirect("/user-profile/" + userUpdateBindingModel.getId());
        }
        if (userUpdateBindingModel.getId() == null){
            userUpdateBindingModel.setId(id);
        }
        this.userService.updateUser(userUpdateBindingModel);
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("{id}/check-in")
    public ModelAndView checkInUser(@PathVariable String id) {
        this.userEntryService.checkInUser(Long.parseLong(id));
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("{entryId}/{id}/remove-entry")
    public ModelAndView removeEntry(@PathVariable String entryId, @PathVariable String id) {
        this.userEntryService.removeLastEntry(Long.parseLong(entryId), Long.parseLong(id));
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("{id}/renew-subscription")
    public ModelAndView renewSubscription(@PathVariable String id, @RequestParam String subscriptionType) {
        this.userService.renewSubscription(id, subscriptionType);
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("{id}/add-minutes")
    public ModelAndView addSolariumMinutes(@PathVariable String id, @RequestParam String minutes) {
        this.userService.addSolariumMinutes(id, Integer.parseInt(minutes));
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("{id}/use-minutes")
    public ModelAndView reduceSolariumMinutes(@PathVariable String id, @RequestParam String minutes) {
        this.userService.useSolariumMinutes(id, Integer.parseInt(minutes));
        return this.redirect("/user-profile/" + id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping(value = "/{id}",params = "delete")
    public ModelAndView deleteUser(@PathVariable String id) throws ResourceNotFoundException {
        this.userService.deleteUser(id);
        return this.redirect("/admin/home");
    }

}
