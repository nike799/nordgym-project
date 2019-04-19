package com.nordgym.service;

import com.nordgym.error.ResourceNotFoundException;
import com.nordgym.domain.models.binding.UserUpdateBindingModel;
import com.nordgym.domain.models.service.UserServiceModel;
import com.nordgym.domain.models.view.UserViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {
    UserViewModel createUserViewModel(UserServiceModel userServiceModel);

    UserServiceModel createUser(UserServiceModel userServiceModel, String subscriptionType);

    UserServiceModel updateUser(UserUpdateBindingModel userUpdateBindingModel) throws NoSuchFieldException, IllegalAccessException;

    List<UserViewModel> getAllUserViewModels();

    UserServiceModel getUserById(Long userId);

    UserServiceModel getUserByUsername(String username) throws ResourceNotFoundException;

    UserUpdateBindingModel getUserUpdateBindingModelByUserId(Long userId);

    void renewSubscription(Long userId, String subscriptionType);

    void addSolariumMinutes(Long userId, int minutes);

    void useSolariumMinutes(Long userId, int minutes);

    List<UserViewModel> getSearchedUsers(String criteria);
    List<UserViewModel> getAllAdmins();

    void deleteUser(Long userId) throws ResourceNotFoundException;

    void editUserProfileImage(Long id, MultipartFile file) throws IOException;
}
