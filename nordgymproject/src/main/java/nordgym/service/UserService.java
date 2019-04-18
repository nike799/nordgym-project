package nordgym.service;

import nordgym.domain.models.binding.UserUpdateBindingModel;
import nordgym.domain.models.service.UserServiceModel;
import nordgym.domain.models.view.UserViewModel;
import nordgym.error.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserViewModel createUserViewModel(UserServiceModel userServiceModel);

    UserServiceModel createUser(UserServiceModel userServiceModel, String subscriptionType);

    UserServiceModel updateUser(UserUpdateBindingModel userUpdateBindingModel) throws NoSuchFieldException, IllegalAccessException;

    List<UserViewModel> getAllUserViewModels();

    UserServiceModel getUserById(String userId);

    UserServiceModel getUserByUsername(String username) throws ResourceNotFoundException;

    UserUpdateBindingModel getUserUpdateBindingModelByUserId(String userId);

    void renewSubscription(String userId, String subscriptionType);

    void addSolariumMinutes(String userId, int minutes);

    void useSolariumMinutes(String userId, int minutes);

    List<UserViewModel> getSearchedUsers(String criteria);
    List<UserViewModel> getAllAdmins();

    void deleteUser(String userId) throws ResourceNotFoundException;

}
