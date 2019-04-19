package com.nordgym.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "solarium_subscriptions")
public class SolariumSubscription extends BaseEntity {
    private int minutes;

    public SolariumSubscription() {
    }

    @Column(name = "minutes")
    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
