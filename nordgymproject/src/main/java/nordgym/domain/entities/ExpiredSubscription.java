package nordgym.domain.entities;
import nordgym.domain.enums.SubscriptionType;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "expired_subscriptions")
public class ExpiredSubscription extends BaseEntity{
    private SubscriptionType subscriptionType;
    private BigDecimal price;
    private Integer countEntries;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<User> users;

    public ExpiredSubscription() {
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

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "expired_subscriptions_users",
    joinColumns = @JoinColumn(name = "expired_subscription_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"))
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

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
