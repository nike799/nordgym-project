package com.nordgym.service;

import com.nordgym.domain.entities.Subscription;
import com.nordgym.domain.entities.User;
import com.nordgym.domain.entities.UserEntry;
import com.nordgym.domain.enums.SubscriptionType;
import com.nordgym.errors.ResourceNotFoundException;
import com.nordgym.repository.ExpiredSubscriptionRepository;
import com.nordgym.repository.SubscriptionRepository;
import com.nordgym.repository.UserEntryRepository;
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
import java.util.Random;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserEntryServiceImplTest {
    @Autowired
    private UserEntryRepository userEntryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ExpiredSubscriptionRepository expiredSubscriptionRepository;

    private UserEntryServiceImpl userEntryService;

    @Before
    public void setUp() throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        this.userEntryService = new UserEntryServiceImpl(
                this.userEntryRepository,
                this.userRepository,
                this.expiredSubscriptionRepository,
                modelMapper);
    }

    @Test
    public void checkInUser_isSuccessful() {
        User user = createUser();
        this.userEntryService.checkInUser(user.getId());
        this.userEntryService.checkInUser(user.getId());

        Assert.assertEquals(2, user.getEntries().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void checkInUser_invalidUserId_throws_Exception() {
        this.userEntryService.checkInUser(100L);
    }

    @Test
    public void removeLastEntry_isSuccessful() {
        User user = createUser();
        UserEntry userEntry = new UserEntry();
        userEntry.setDateAndTimeOfUserEntry(LocalDateTime.now());
        this.userEntryRepository.save(userEntry);
        user.getEntries().add(userEntry);
        this.userRepository.save(user);

        Assert.assertEquals(1, this.userEntryRepository.count());
        Assert.assertEquals(1, user.getEntries().size());
        this.userEntryService.removeLastEntry(userEntry.getId(), user.getId());
        Assert.assertEquals(0, this.userEntryRepository.count());
        Assert.assertEquals(0, user.getEntries().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void removeLastEntry_invalidUserId_throws_Exception() {
        UserEntry userEntry = new UserEntry();
        userEntry.setDateAndTimeOfUserEntry(LocalDateTime.now());
        this.userEntryRepository.save(userEntry);

        this.userEntryService.removeLastEntry(100L, userEntry.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void removeLastEntry_invalidUserEntryId_throws_Exception() {
        User user = createUser();
        UserEntry userEntry = new UserEntry();
        userEntry.setDateAndTimeOfUserEntry(LocalDateTime.now());
        this.userEntryRepository.save(userEntry);
        user.getEntries().add(userEntry);
        this.userRepository.save(user);

        this.userEntryService.removeLastEntry(user.getId(), 100L);
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