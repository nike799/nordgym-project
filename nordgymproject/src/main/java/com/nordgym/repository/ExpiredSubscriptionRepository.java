package com.nordgym.repository;

import com.nordgym.domain.entities.ExpiredSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpiredSubscriptionRepository extends JpaRepository<ExpiredSubscription,Long> {
}
