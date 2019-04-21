package com.nordgym.web.controllers;

import com.nordgym.domain.entities.Subscription;
import com.nordgym.domain.entities.User;
import com.nordgym.domain.enums.SubscriptionType;
import com.nordgym.repository.SubscriptionRepository;
import com.nordgym.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserProfileControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void getUserProfile() {
    }

    @Test
    @WithMockUser(authorities = {"USER","ADMIN"})
    public void editUser_confirmEditUser() throws Exception {
       User user = createUser();
       this.mvc
                .perform(post("/user-profile/" + user.getId())
                        .param("firstName", "Nikola"));
        Assert.assertEquals("Nikola",this.userRepository.findById(user.getId()).get().getFirstName());
    }

    @Test
    public void editProfileImage() {
    }

    @Test
    public void checkInUser() {
    }

    @Test
    public void removeEntry() {
    }

    @Test
    public void renewSubscription() {
    }

    @Test
    public void addSolariumMinutes() {
    }

    @Test
    public void reduceSolariumMinutes() {
    }

    @Test
    public void deleteUser() {
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