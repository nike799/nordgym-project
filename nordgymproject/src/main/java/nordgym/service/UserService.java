package nordgym.service;


import nordgym.domain.models.service.UserServiceModel;
import nordgym.domain.models.view.UserViewModel;
import nordgym.domain.models.binding.UserRegisterBindingModel;
import nordgym.domain.models.binding.UserUpdateBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
  UserViewModel createUserViewModel(UserServiceModel userServiceModel);
  boolean createUser(UserRegisterBindingModel userRegisterBindingModel);
  boolean updateUser(UserUpdateBindingModel userUpdateBindingModel);
  List<UserViewModel> getAllUserViewModels();
  UserServiceModel getUserById(String userId);
  UserServiceModel getUserByUsername(String username);
  UserUpdateBindingModel getUserUpdateBindingModelByUserId(String userId);
  UserServiceModel getUserBySubscriptionNumber(String subscriptionNumber);
  void renewSubscription(String userId,String subscriptionType);
  void addSolariumMinutes(String userId, int minutes);
  void reduceSolariumMinutes(String userId, int minutes);
  List<UserViewModel> getSearchedUsers(String criteria);

}
