package com.nordgym.repository;

import com.nordgym.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users u ORDER by u.first_name ASC,u.last_name ASC", nativeQuery = true)
    List<User> getAllUsersOrderedByName();

    @Query(value = "SELECT * FROM users u \n" +
            "       JOIN users_authorities ua ON u.id = ua.user_id \n" +
            "WHERE ua.role_id = 2 \n" +
            "ORDER BY u.first_name ASC, u.last_name ASC", nativeQuery = true)
    List<User> getAllAdminsOrderedByName();

    Optional<User> findBySubscriptionNumberIsLike(String subscriptionNumber);

}
