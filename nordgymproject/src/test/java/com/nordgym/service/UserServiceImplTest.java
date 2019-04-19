package com.nordgym.service;

import com.nordgym.domain.entities.ExpiredSubscription;
import com.nordgym.domain.entities.Role;
import com.nordgym.domain.entities.Subscription;
import com.nordgym.domain.entities.User;
import com.nordgym.domain.enums.SubscriptionType;
import com.nordgym.domain.models.binding.UserUpdateBindingModel;
import com.nordgym.domain.models.service.UserServiceModel;
import com.nordgym.domain.models.view.UserViewModel;
import com.nordgym.error.ResourceNotFoundException;
import com.nordgym.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceImplTest {
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SolariumSubscriptionRepository solariumSubscriptionRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ExpiredSubscriptionRepository expiredSubscriptionRepository;
    @Autowired
    private UserEntryRepository userEntryRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;


    @Before
    public void setUp() throws Exception {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.modelMapper = new ModelMapper();
        this.userService = new UserServiceImpl(
                userRepository, roleRepository,
                solariumSubscriptionRepository,
                subscriptionRepository,
                expiredSubscriptionRepository,
                userEntryRepository,
                bCryptPasswordEncoder,
                modelMapper
        );
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void createUser_isSuccess() {
        UserServiceModel expected = this.userService.createUser(this.getUserServiceModel(), null);
        UserServiceModel actual = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(expected.getSubscriptionNumber(), actual.getSubscriptionNumber());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getProfileImagePath(), actual.getProfileImagePath());
    }

    @Test
    public void updateUser_isSuccess() throws NoSuchFieldException, IllegalAccessException {
        User user = createUser();
        UserUpdateBindingModel userUpdateBindingModel = this.userUpdateBindingModel();
        userUpdateBindingModel.setId(user.getId());

        UserServiceModel expected = this.userService.updateUser(userUpdateBindingModel);
        UserServiceModel actual = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(expected.getSubscriptionNumber(), actual.getSubscriptionNumber());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getProfileImagePath(), actual.getProfileImagePath());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateUser_ifNotSuccess_throws_ResourceNotFoundException() throws NoSuchFieldException, IllegalAccessException {
        UserUpdateBindingModel userUpdateBindingModel = this.userUpdateBindingModel();
        userUpdateBindingModel.setId(100L);
        this.userService.updateUser(userUpdateBindingModel);
    }

    @Test
    public void loadUserByUsername() {
    }

    @Test
    public void getAllUserViewModels() {
        createUser();
        createUser();
        createUser();
        createUser();
        createUser();
        createNotAdminUser();
        createNotAdminUser();
        createNotAdminUser();
        List<UserViewModel> users = this.userService.getAllUserViewModels();

        Assert.assertEquals(3, users.size());
    }


    @Test
    public void getUserById_returnsCorrect() {
        User user = createUser();
        UserServiceModel expected = this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
        UserServiceModel actual = this.userService.getUserById(expected.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getSubscriptionNumber(), actual.getSubscriptionNumber());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserById_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.getUserById(100L);
    }

    @Test
    public void getUserByUsername_returnsCorrect() {
        User user = createUser();
        user.setUsername("nike");
        UserServiceModel expected = this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
        UserServiceModel actual = this.userService.getUserByUsername(expected.getUsername());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getSubscriptionNumber(), actual.getSubscriptionNumber());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByUsername_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.getUserByUsername("noUser");
    }

    @Test
    public void getUserUpdateBindingModelByUserId() {
        User user = createUser();
        UserUpdateBindingModel expected = this.modelMapper.map(this.userRepository.save(user), UserUpdateBindingModel.class);
        UserUpdateBindingModel actual = this.userService.getUserUpdateBindingModelByUserId(user.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getSubscriptionNumber(), actual.getSubscriptionNumber());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserUpdateBindingModelByUserId_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.getUserUpdateBindingModelByUserId(100L);
    }

    @Test
    public void renewSubscription_isSuccess() {
        User user = createUser();
        Subscription subscription = getSubscription();
        user.setSubscription(subscription);
        user = this.userRepository.save(user);

        this.userService.renewSubscription(user.getId(), SubscriptionType.TWELVE_ENTRIES.name());

        Assert.assertEquals(12L, ((long) user.getSubscription().getCountEntries()));
        Assert.assertEquals(SubscriptionType.TWELVE_ENTRIES, user.getSubscription().getSubscriptionType());
        Assert.assertEquals(BigDecimal.valueOf(30), user.getSubscription().getPrice());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void renewSubscription_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.renewSubscription(100L, SubscriptionType.TWELVE_ENTRIES.name());
    }

    private Subscription getSubscription() {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(SubscriptionType.SIX_ENTRIES);
        subscription.setCountEntries(6);
        subscription.setPrice(BigDecimal.TEN);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        subscription = this.subscriptionRepository.save(subscription);
        return subscription;
    }

    @Test
    public void addSolariumMinutes_isSuccess() {
        User user = createUser();
        this.userService.addSolariumMinutes(user.getId(), 10);

        Assert.assertEquals(10, user.getSolariumSubscription().getMinutes());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addSolariumMinutes_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.addSolariumMinutes(100L, 100);
    }

    @Test
    public void useSolariumMinutes() {
        User user = createUser();
        this.userService.addSolariumMinutes(user.getId(), 10);
        this.userService.useSolariumMinutes(user.getId(), 10);

        Assert.assertEquals(0, user.getSolariumSubscription().getMinutes());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void useSolariumMinutes_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.useSolariumMinutes(100L, 5);
    }

    @Test
    public void getSearchedUsers() {
        createNotAdminUser();
        createNotAdminUser();
       User user =  createNotAdminUser();
       user.setFirstName("Some");
       user.setLastName("Name");
       this.userRepository.save(user);

        List<UserViewModel> models = this.userService.getSearchedUsers("Some Name");
        Assert.assertEquals(1,models.size());
        Assert.assertEquals("Some Name", models.get(0).getFullName());
    }

    @Test
    public void getAllAdmins() {
        createUser();
        createNotAdminUser();
        createNotAdminUser();
        List<UserViewModel> admins = this.userService.getAllAdmins();
        Assert.assertEquals(1, admins.size());
    }

    @Test
    public void deleteUser_isSuccess() {
        User user = createUser();
        ExpiredSubscription expiredSubscription = new ExpiredSubscription();
        expiredSubscription = this.expiredSubscriptionRepository.save(expiredSubscription);
        Subscription subscription = getSubscription();

        user.setExpiredSubscriptions(new HashSet<>());
        user.getExpiredSubscriptions().add(expiredSubscription);
        user.setSubscription(subscription);
        user = this.userRepository.save(user);
        this.userService.deleteUser(user.getId());

        Assert.assertTrue(this.userEntryRepository.findById(user.getId()).isEmpty());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUser_ifNotSuccess_throws_ResourceNotFoundException() {
        this.userService.deleteUser(100L);
    }

    @Test
    public void createUserViewModel_isSuccess() {
        UserServiceModel userServiceModel = this.modelMapper.map(createUser(), UserServiceModel.class);
        UserViewModel userViewModel = this.userService.createUserViewModel(userServiceModel);

        Assert.assertEquals(userServiceModel.getFirstName(), userViewModel.getFullName().split("\\s+")[0]);
        Assert.assertEquals(userServiceModel.getLastName(), userViewModel.getFullName().split("\\s+")[1]);
        Assert.assertEquals(userServiceModel.getSubscriptionNumber(), userViewModel.getSubscriptionNumber());
        Assert.assertEquals("No info", userViewModel.getEmail());
    }

    private UserServiceModel getUserServiceModel() {
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setSubscriptionNumber("00000015");
        userServiceModel.setFirstName("Emil");
        userServiceModel.setLastName("Kostov");
        userServiceModel.setProfileImagePath("emo.jpg");
        userServiceModel.setUsername("emsi");
        userServiceModel.setPassword("123");
        userServiceModel.setEmail("emo@gmail.com");
        userServiceModel.setAdmin(true);
        return userServiceModel;
    }

    private UserUpdateBindingModel userUpdateBindingModel() {
        UserUpdateBindingModel userUpdateBindingModel = new UserUpdateBindingModel();
        userUpdateBindingModel.setSubscriptionNumber("00000018");
        userUpdateBindingModel.setFirstName("Pavel");
        userUpdateBindingModel.setLastName("Tsekov");
        userUpdateBindingModel.setUsername("pavelino");
        userUpdateBindingModel.setPassword("321");
        userUpdateBindingModel.setEmail("pavelino@gmail.com");
        userUpdateBindingModel.setAdmin(false);
        return userUpdateBindingModel;
    }

    private void setRolesToUser(User user) {
        Role userRole = new Role();
        userRole.setAuthority("USER");
        Role adminRole = new Role();
        adminRole.setAuthority("ADMIN");
        Set<Role> authorities = new HashSet<>() {{
            add(userRole);
            add(adminRole);
        }};
        this.roleRepository.saveAll(authorities);
        user.setAuthorities(authorities);
    }

    private User createUser() {
        User user = new User();
        setRolesToUser(user);
        Random random = new Random();
        int number = random.nextInt();
        user.setSubscriptionNumber("001" + number);
        user.setFirstName("Niki");
        user.setLastName("Piki");
        user.setProfileImagePath("niki.jpg");
        this.userRepository.save(user);
        return user;
    }

    private User createNotAdminUser() {
        User user = new User();
        Role userRole = new Role();
        userRole.setAuthority("USER");
        Set<Role> authorities = new HashSet<>() {{
            add(userRole);
        }};
        this.roleRepository.saveAll(authorities);
        user.setAuthorities(authorities);

        Random random = new Random();
        int number = random.nextInt();
        user.setSubscriptionNumber("001" + number);
        user.setFirstName("Niki");
        user.setLastName("Piki");
        user.setProfileImagePath("niki.jpg");
        this.userRepository.save(user);
        return user;
    }
}