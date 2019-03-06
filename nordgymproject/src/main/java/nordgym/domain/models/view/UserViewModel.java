package nordgym.domain.models.view;
import nordgym.domain.entities.SolariumSubscription;
import nordgym.domain.entities.UserEntry;

import java.time.LocalDateTime;
import java.util.Set;

public class UserViewModel {
    private Long id;
    private String fullName;
    private String subscriptionNumber;
    private String profileImagePath;
    private String subscription;
    private SolariumSubscription solariumSubscription;
    private LocalDateTime subscriptionFrom;
    private LocalDateTime subscriptionTo;
    private Integer entriesLeft;
    private String email;
    private Set<UserEntry> entries;
    private boolean isActive;

    public UserViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public SolariumSubscription getSolariumSubscription() {
        return solariumSubscription;
    }

    public void setSolariumSubscription(SolariumSubscription solariumSubscription) {
        this.solariumSubscription = solariumSubscription;
    }

    public LocalDateTime getSubscriptionFrom() {
        return subscriptionFrom;
    }

    public void setSubscriptionFrom(LocalDateTime subscriptionFrom) {
        this.subscriptionFrom = subscriptionFrom;
    }

    public LocalDateTime getSubscriptionTo() {
        return subscriptionTo;
    }

    public void setSubscriptionTo(LocalDateTime subscriptionTo) {
        this.subscriptionTo = subscriptionTo;
    }

    public Integer getEntriesLeft() {
        return entriesLeft;
    }

    public void setEntriesLeft(Integer entriesLeft) {
        this.entriesLeft = entriesLeft;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<UserEntry> entries) {
        this.entries = entries;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
