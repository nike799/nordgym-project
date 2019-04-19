package com.nordgym.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "entries")
public class UserEntry extends BaseEntity {
    private LocalDateTime dateAndTimeOfUserEntry;
    private Set<User> users;

    public UserEntry() {
        this.users = new LinkedHashSet<>();
    }

    @Column(name = "date_and_time_of_entry")
    public LocalDateTime getDateAndTimeOfUserEntry() {
        return dateAndTimeOfUserEntry;
    }

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "entries_users",
            joinColumns = @JoinColumn(
                    name = "entry_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    public Set<User> getUsers() {
        return users;
    }

    public void setDateAndTimeOfUserEntry(LocalDateTime dateAndTimeOfUserEntry) {
        this.dateAndTimeOfUserEntry = dateAndTimeOfUserEntry;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
