package com.nordgym.service;

import com.nordgym.domain.entities.Subscription;
import com.nordgym.domain.entities.User;
import com.nordgym.domain.enums.SubscriptionType;
import com.nordgym.repository.ExpiredSubscriptionRepository;
import com.nordgym.repository.SubscriptionRepository;
import com.nordgym.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SubscriptionServiceImplTest {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ExpiredSubscriptionRepository expiredSubscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    private SubscriptionServiceImpl subscriptionService;

    @Before
    public void setUp() throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        this.subscriptionService = new SubscriptionServiceImpl(
                this.subscriptionRepository,
                this.expiredSubscriptionRepository,
                this.userRepository,
                modelMapper);
    }

    @Test
    public void removeExpiredSubscriptions_isSuccessful() {
        User user = createUser();
        user.getSubscription().setEndDate(LocalDateTime.now().minusMonths(1));
        this.userRepository.save(user);
        user.getSubscription().setUsers(new HashSet<>());
        user.getSubscription().getUsers().add(user);
        this.subscriptionRepository.save(user.getSubscription());

        Assert.assertEquals(1, this.subscriptionRepository.count());
        this.subscriptionService.removeExpiredSubscriptions();
        Assert.assertEquals(0, this.subscriptionRepository.count());
    }

    private User createUser() {
        User user = new User();
        Random random = new Random();
        int number = random.nextInt();
        user.setSubscriptionNumber("001" + number);
        user.setFirstName("Niki");
        user.setLastName("Piki");
        Subscription subscription = createSubscription();
        user.setSubscription(subscription);
        this.userRepository.save(user);
        return user;
    }

    private Subscription createSubscription() {
        Subscription subscription = new Subscription();
        subscription.setCountEntries(8);
        subscription.setPrice(BigDecimal.valueOf(26));
        subscription.setSubscriptionType(SubscriptionType.EIGHT_ENTRIES);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        return this.subscriptionRepository.save(subscription);
    }
}