package nordgym.domain.entities;

import nordgym.domain.enums.SubscriptionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "subscriptions")
public class Subscription extends BaseEntity {
    private SubscriptionType subscriptionType;
    private BigDecimal price;
    private Integer countEntries;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isExpired;
    private Set<User> users;

    public Subscription() {
    }

    @Enumerated(EnumType.STRING)
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    @Column(name = "count_of_entries")
    public Integer getCountEntries() {
        return countEntries;
    }

    @Column(name = "start_date")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Column(name = "end_date")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Column(name = "is_expired")
    public Boolean getExpired() {
        return this.endDate.isAfter(LocalDateTime.now()) && this.countEntries > 0;
    }

    @OneToMany(targetEntity = User.class, mappedBy = "subscription")
    public Set<User> getUsers() {
        return users;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCountEntries(Integer countEntries) {
        this.countEntries = countEntries;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
