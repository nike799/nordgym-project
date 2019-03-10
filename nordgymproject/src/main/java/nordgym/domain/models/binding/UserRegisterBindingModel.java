package nordgym.domain.models.binding;

import nordgym.annotation.UniqueUser;
import nordgym.domain.entities.SolariumSubscription;
import nordgym.domain.entities.Subscription;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRegisterBindingModel {
    private String subscriptionNumber;
    private String firstName;
    private String lastName;
    private String subscription;
    private SolariumSubscription solariumSubscription;
    private String profileImagePath;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

    public UserRegisterBindingModel() {
    }

    @Pattern(regexp = "\\d+", message = "Subscription must contain only numbers")
    @NotNull(message = "Subscription number can not be empty")
    @UniqueUser(message = "There is already registered user with this subscription number")
    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    @Pattern(regexp = "^[А-Я|A-Z][а-я|a-z]+$", message = "First name must begin with capital letter and contain only letters")
    @NotNull(message = "first name can not be empty")
    public String getFirstName() {
        return firstName;
    }

    @Pattern(regexp = "^[А-Я|A-Z][а-я|a-z]+$", message = "Last name must begin with capital letter and contain only letters")
    @NotNull(message = "Last name can not be empty")
    public String getLastName() {
        return lastName;
    }

    @NotNull(message = "Subscription can not be empty")
    public String getSubscription() {
        return subscription;
    }

    public SolariumSubscription getSolariumSubscription() {
        return solariumSubscription;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public void setSolariumSubscription(SolariumSubscription solariumSubscription) {
        this.solariumSubscription = solariumSubscription;
    }

    public void setProfileImagePath(String profileImage) {
        this.profileImagePath = profileImage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
