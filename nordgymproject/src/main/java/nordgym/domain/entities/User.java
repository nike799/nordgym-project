package nordgym.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "users")
public class User extends BaseEntity implements UserDetails {
    private String subscriptionNumber;
    private String firstName;
    private String lastName;
    private String profileImagePath;
    private Subscription subscription;
    private SolariumSubscription solariumSubscription;
    private String username;
    private String password;
    private String email;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;
    private Set<Role> authorities;
    private Set<UserEntry> entries;
    private Set<ExpiredSubscription> expiredSubscriptions;

    public User() {
        this.authorities = new HashSet<>();
        this.entries = new LinkedHashSet<>();
    }

    @Column(name = "subscription_number", nullable = false, unique = true)
    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    @Column(name = "first_name", nullable = false,columnDefinition = "text")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", nullable = false,columnDefinition = "text")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "profile_image_path")
    public String getProfileImagePath() {
        return profileImagePath;
    }

    @ManyToOne(targetEntity = Subscription.class)
    public Subscription getSubscription() {
        return subscription;
    }

    @ManyToOne(targetEntity = SolariumSubscription.class)
    public SolariumSubscription getSolariumSubscription() {
        return solariumSubscription;
    }

    @Override
    @Column(name = "password")
    public String getPassword() {
        return this.password;
    }

    @Override
    @Column(name = "username",unique = true)
    public String getUsername() {
        return this.username;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Override
    @Column(name = "is_account_non_expired")
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @Column(name = "is_account_non_locked")
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @Column(name = "is_credential_non_expired")
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @Column(name = "is_enabled")
    public boolean isEnabled() {
        return false;
    }

    @Override
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    @ManyToMany(targetEntity = UserEntry.class, mappedBy = "users")
    public Set<UserEntry> getEntries() {
        return entries;
    }

    @ManyToMany(targetEntity = ExpiredSubscription.class, mappedBy = "users")
    public Set<ExpiredSubscription> getExpiredSubscriptions() {
        return expiredSubscriptions;
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

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public void setSolariumSubscription(SolariumSubscription solariumSubscription) {
        this.solariumSubscription = solariumSubscription;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public void setEntries(Set<UserEntry> entries) {
        this.entries = entries;
    }

    public void setExpiredSubscriptions(Set<ExpiredSubscription> expiredSubscriptions) {
        this.expiredSubscriptions = expiredSubscriptions;
    }
}
