package nordgym.domain.models.binding;

import nordgym.constants.GlobalConstants;
import nordgym.annotation.UniqueSubscriptionNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserUpdateBindingModel {
    private String id;
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

    public String getId() {
        return id;
    }


    @Pattern(regexp = "\\d+", message = GlobalConstants.SUBSCRIPTION_NUMBER_MUST_CONTAINS_ONLY_NUMBERS)
    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @UniqueSubscriptionNumber(message = GlobalConstants.THERE_IS_ALREADY_REGISTERED_USER_WITH_THIS_SUBSCRIPTION_NUMBER)
    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }


    @Pattern(regexp = "^[А-Я|A-Z][а-я|a-z]+$", message = GlobalConstants.FIRST_NAME_MUST_BEGIN_WITH_CAPITAL_LETTER_AND_CONTAINS_ONLY_LETTERS)
    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getFirstName() {
        return firstName;
    }


    @Pattern(regexp = "^[А-Я|A-Z][а-я|a-z]+$", message = GlobalConstants.LAST_NAME_MUST_BEGIN_WITH_CAPITAL_LETTER_AND_CONTAINS_ONLY_LETTERS)
    @NotNull(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    @NotEmpty(message = GlobalConstants.THIS_FIELD_IS_OBLIGATORY)
    public String getLastName() {
        return lastName;
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

    public Boolean getAdmin() {
        return isAdmin;
    }
    public void setId(String id) {
        this.id = id;
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

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
