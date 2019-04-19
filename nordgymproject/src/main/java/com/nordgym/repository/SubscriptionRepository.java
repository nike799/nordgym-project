package com.nordgym.repository;

import com.nordgym.domain.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

Optional<List<Subscription>> findAllByEndDateIsBefore(LocalDateTime today);
}
