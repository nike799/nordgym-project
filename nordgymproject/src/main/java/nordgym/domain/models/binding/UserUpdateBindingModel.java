package nordgym.domain.models.binding;

import nordgym.annotation.UniqueUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserUpdateBindingModel {
    private String userId;
    private String subscriptionNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Boolean isAdmin;

    public UserUpdateBindingModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Pattern(regexp = "\\d+", message = "Subscription must contain only numbers")
    @NotNull(message = "Subscription number can not be empty")
    @UniqueUser(message = "There is already registered user with this subscription number")
    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    @Pattern(regexp = "[A-Z][A-Za-z]+", message = "First name must begin with capital letter and contain only letters")
    @NotNull(message = "first name can not be empty")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Pattern(regexp = "[A-Z][A-Za-z]+", message = "Last name must begin with capital letter and contain only letters")
    @NotNull(message = "Last name can not be empty")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
