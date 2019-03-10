package nordgym.web.controllers;

import nordgym.domain.models.binding.UserUpdateBindingModel;
import nordgym.domain.models.view.UserViewModel;
import nordgym.service.UserEntryService;
import nordgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequestMapping({"/user-profile", "/user-profile/{userId}"})
public class UserProfileController extends BaseController {
    private final UserService userService;
    private final UserEntryService userEntryService;

    @Autowired
    public UserProfileController(UserService userService, UserEntryService userEntryService) {
        this.userService = userService;
        this.userEntryService = userEntryService;
    }

    @GetMapping("/{userId}")
    public ModelAndView getUserProfile(@PathVariable String userId, ModelAndView modelAndView, Authentication authentication) {

        UserViewModel userViewModel = this.userService.createUserViewModel(this.userService.getUserById(userId));
        UserUpdateBindingModel userUpdateBindingModel = this.userService.getUserUpdateBindingModelByUserId(userId);
        modelAndView.addObject("username", authentication.getName());
        modelAndView.addObject("userViewModel", userViewModel);
        modelAndView.addObject("userId", userViewModel.getId());
        modelAndView.addObject("userUpdateBindingModel", userUpdateBindingModel);
        return this.view("/user-profile", modelAndView);
    }

    @PostMapping("/{userId}")
    public ModelAndView editUser(@ModelAttribute UserUpdateBindingModel userUpdateBindingModel, @PathVariable String userId, BindingResult bindingResult) {
        if (!userUpdateBindingModel.getPassword().equals(userUpdateBindingModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", userUpdateBindingModel.getConfirmPassword(), "Confirmed password is not equal to password!");
        }
        if (bindingResult.hasErrors()) {
            return this.redirect("/user-profile/" + userUpdateBindingModel.getUserId());
        }
        this.userService.updateUser(userUpdateBindingModel);
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{userId}/check-in")
    public ModelAndView checkInUser(@PathVariable String userId) {
        this.userEntryService.checkInUser(Long.parseLong(userId));
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{entryId}/{userId}/remove-entry")
    public ModelAndView removeEntry(@PathVariable String entryId, @PathVariable String userId) {
        this.userEntryService.removeLastEntry(Long.parseLong(entryId), Long.parseLong(userId));
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{userId}/renew-subscription")
    public ModelAndView renewSubscription(@PathVariable String userId, @RequestParam String subscriptionType) {
        this.userService.renewSubscription(userId, subscriptionType);
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{userId}/add-minutes")
    public ModelAndView addSolariumMinutes(@PathVariable String userId, @RequestParam String minutes) {
        this.userService.addSolariumMinutes(userId, Integer.parseInt(minutes));
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{userId}/use-minutes")
    public ModelAndView reduceSolariumMinutes(@PathVariable String userId, @RequestParam String minutes) {
        this.userService.useSolariumMinutes(userId, Integer.parseInt(minutes));
        return this.redirect("/user-profile/" + userId);
    }

    @PostMapping("{userId}/user-delete")
    public ModelAndView deleteUser(@PathVariable String userId) {
        this.userService.deleteUser(userId);
        return this.redirect("/home");
    }
}
