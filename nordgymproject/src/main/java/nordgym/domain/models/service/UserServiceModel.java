package nordgym.domain.models.service;

import nordgym.domain.entities.Role;
import nordgym.domain.entities.SolariumSubscription;
import nordgym.domain.entities.Subscription;
import nordgym.domain.entities.UserEntry;

import java.util.Set;

public class UserServiceModel {
    private Long id;
    private String subscriptionNumber;
    private String firstName;
    private String lastName;
    private String profileImagePath;
    private Subscription subscription;
    private SolariumSubscription solariumSubscription;
    private String username;
    private String password;
    private String email;
    private Boolean isAdmin;
    private Set<Role> authorities;
    private Set<UserEntry> entries;

    public UserServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public SolariumSubscription getSolariumSubscription() {
        return solariumSubscription;
    }

    public void setSolariumSubscription(SolariumSubscription solariumSubscription) {
        this.solariumSubscription = solariumSubscription;
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

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public Set<UserEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<UserEntry> entries) {
        this.entries = entries;
    }
}
