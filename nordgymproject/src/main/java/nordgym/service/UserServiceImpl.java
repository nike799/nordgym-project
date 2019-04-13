package nordgym.service;

import nordgym.GlobalConstants;
import nordgym.domain.entities.*;
import nordgym.domain.enums.SubscriptionType;
import nordgym.domain.models.binding.UserUpdateBindingModel;
import nordgym.domain.models.service.UserServiceModel;
import nordgym.domain.models.view.UserViewModel;
import nordgym.error.ResourceNotFoundException;
import nordgym.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SolariumSubscriptionRepository solariumSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ExpiredSubscriptionRepository expiredSubscriptionRepository;
    private final UserEntryRepository userEntryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           SolariumSubscriptionRepository solariumSubscriptionRepository, SubscriptionRepository subscriptionRepository,
                           ExpiredSubscriptionRepository expiredSubscriptionRepository, UserEntryRepository userEntryRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.solariumSubscriptionRepository = solariumSubscriptionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.expiredSubscriptionRepository = expiredSubscriptionRepository;
        this.userEntryRepository = userEntryRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean createUser(UserServiceModel userServiceModel, String subscriptionType) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        this.setUserRoles(user, userServiceModel.getAdmin());
        if (subscriptionType != null) {
            Subscription subscription = createSubscription(subscriptionType);
            user.setSubscription(subscription);
            this.subscriptionRepository.save(subscription);
        }
        return this.userRepository.save(user) != null;
    }

    @Override
    public boolean updateUser(UserUpdateBindingModel userUpdateBindingModel) {
        User user = this.userRepository.findById(Long.parseLong(userUpdateBindingModel.getUserId())).orElse(null);

        if (user != null) {
            String password = user.getPassword();
            this.modelMapper.map(userUpdateBindingModel, user);
            if (userUpdateBindingModel.getPassword().isEmpty()) {
                user.setPassword(password);
            } else {
                user.setPassword(this.bCryptPasswordEncoder.encode(userUpdateBindingModel.getPassword()));
            }
            if (user.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals("ROOT_ADMIN"))) {
                if (userUpdateBindingModel.getAdmin() != null) {
                    this.setUserRoles(user, userUpdateBindingModel.getAdmin());
                }
            }
        }
        return this.userRepository.save(user) != null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        this.seedRoles();
        this.seedRootAdmin();
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

    @Override
    public List<UserViewModel> getAllUserViewModels() {
        return this.userRepository.count() > 1 ? this.userRepository.getAllUsersOrderedByName().
                stream().
                filter(user -> user.getAuthorities().
                        stream().noneMatch(role -> role.getAuthority().equals("ADMIN"))).
                map(user -> this.createUserViewModel(this.modelMapper.map(user, UserServiceModel.class))).collect(Collectors.toList()) :
                new LinkedList<>();
    }

    @Override
    public UserServiceModel getUserById(String userId) {
        User user = getUser(userId);
        return  this.modelMapper.map(user, UserServiceModel.class);

    }

    @Override
    public UserServiceModel getUserByUsername(String username) throws ResourceNotFoundException {
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (user == null){
            throw new ResourceNotFoundException(String.format(GlobalConstants.USER_WITH_SUCH_USERNAME_DOESNT_EXISTS,username));
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserUpdateBindingModel getUserUpdateBindingModelByUserId(String userId) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        UserUpdateBindingModel userUpdateBindingModel = null;
        if (user != null) {
            userUpdateBindingModel = this.modelMapper.map(user, UserUpdateBindingModel.class);
            userUpdateBindingModel.setAdmin(user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN")));
        }
        return userUpdateBindingModel;
    }

    @Override
    public void renewSubscription(String userId, String subscriptionType) {
        User user = this.userRepository.findById(Long.parseLong(userId)).get();
        if (user.getSubscription() != null) {
            this.expiredSubscriptionRepository.save(this.modelMapper.map(user.getSubscription(), ExpiredSubscription.class));
            long subscriptionId = user.getSubscription().getId();
            user.setSubscription(null);
            user = this.userRepository.save(user);
            this.subscriptionRepository.deleteById(subscriptionId);
        }
        Subscription subscription = this.createSubscription(subscriptionType);
        subscription = this.subscriptionRepository.save(subscription);
        user.setSubscription(subscription);
        this.userRepository.save(user);

    }

    @Override
    public void addSolariumMinutes(String userId, int minutes) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (user != null) {
            if (user.getSolariumSubscription() != null) {
                user.getSolariumSubscription().setMinutes(user.getSolariumSubscription().getMinutes() + minutes);
            } else {
                SolariumSubscription solariumSubscription = new SolariumSubscription();
                solariumSubscription.setMinutes(minutes);
                user.setSolariumSubscription(this.solariumSubscriptionRepository.save(solariumSubscription));

            }
        }
        this.userRepository.save(user);
    }

    @Override
    public void useSolariumMinutes(String userId, int minutes) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (user != null && user.getSolariumSubscription() != null && user.getSolariumSubscription().getMinutes() >= minutes) {
            user.getSolariumSubscription().setMinutes(user.getSolariumSubscription().getMinutes() - minutes);
        }
        this.userRepository.save(user);
    }

    @Override
    public List<UserViewModel> getSearchedUsers(String criteria) {
        return this.getAllUserViewModels()
                .stream()
                .filter(userViewModel -> userViewModel.getSubscriptionNumber().equals(criteria) || userViewModel.getFullName().toLowerCase().contains(criteria.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserViewModel> getAllAdmins() {
        return this.userRepository.getAllAdminsOrderedByName()
                .stream()
                .filter(user -> user.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals("ROOT_ADMIN")))
                .map(user -> this.createUserViewModel(this.modelMapper.map(user, UserServiceModel.class)))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String userId) throws ResourceNotFoundException {
       User user =  getUser(userId);
        user.getEntries().forEach(this.userEntryRepository::delete);
        user.getExpiredSubscriptions().forEach(this.expiredSubscriptionRepository::delete);
        this.userRepository.delete(user);
        this.subscriptionRepository.deleteById(user.getSubscription().getId());
        if (user.getSolariumSubscription() != null) {
            this.solariumSubscriptionRepository.deleteById(user.getSolariumSubscription().getId());
        }
    }

    @Override
    public UserViewModel createUserViewModel(UserServiceModel userServiceModel) {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.setId(userServiceModel.getId());
        userViewModel.setFullName(userServiceModel.getFirstName().concat(" ").concat(userServiceModel.getLastName()));
        userViewModel.setUsername(userServiceModel.getUsername());
        userViewModel.setProfileImagePath(userServiceModel.getProfileImagePath());
        userViewModel.setSubscriptionNumber(userServiceModel.getSubscriptionNumber());

        if (userServiceModel.getSubscription() != null) {
            userViewModel.setSubscription(this.setSubscriptionName(userServiceModel.getSubscription().getSubscriptionType().name()));
            userViewModel.setSubscriptionFrom(userServiceModel.getSubscription().getStartDate());
            userViewModel.setSubscriptionTo(userServiceModel.getSubscription().getEndDate());
            userViewModel.setEntriesLeft(userServiceModel.getSubscription().getCountEntries());
            userViewModel.setActive(userServiceModel.getSubscription().getExpired());
        }
        userViewModel.setSolariumSubscription(userServiceModel.getSolariumSubscription() != null ? userServiceModel.getSolariumSubscription() : new SolariumSubscription());
        userViewModel.setEmail(userServiceModel.getEmail() != null ? userServiceModel.getEmail() : "No Info");
        if (userServiceModel.getEntries() != null
                && userServiceModel.getEntries().size() > 0 && userServiceModel.getSubscription() != null) {
            userViewModel.setEntries(userServiceModel.getEntries().
                    stream()
                    .filter(e -> e.getDateAndTimeOfUserEntry().isAfter(userServiceModel.getSubscription().getStartDate()))
                    .sorted((e1, e2) -> e2.getDateAndTimeOfUserEntry().compareTo(e1.getDateAndTimeOfUserEntry())).
                            collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return userViewModel;
    }

    private Subscription createSubscription(String subscriptionType) {
        Subscription subscription = new Subscription();
        switch (subscriptionType) {
            case "SIX_ENTRIES":
                subscription.setCountEntries(6);
                subscription.setPrice(BigDecimal.valueOf(22));
                subscription.setSubscriptionType(SubscriptionType.SIX_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
            case "EIGHT_ENTRIES":
                subscription.setCountEntries(8);
                subscription.setPrice(BigDecimal.valueOf(26));
                subscription.setSubscriptionType(SubscriptionType.EIGHT_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
            case "TWELVE_ENTRIES":
                subscription.setCountEntries(12);
                subscription.setPrice(BigDecimal.valueOf(30));
                subscription.setSubscriptionType(SubscriptionType.TWELVE_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
            case "SIXTEEN_ENTRIES":
                subscription.setCountEntries(16);
                subscription.setPrice(BigDecimal.valueOf(38));
                subscription.setSubscriptionType(SubscriptionType.SIXTEEN_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
            case "TWENTY_FOUR_ENTRIES":
                subscription.setCountEntries(24);
                subscription.setPrice(BigDecimal.valueOf(50));
                subscription.setSubscriptionType(SubscriptionType.TWENTY_FOUR_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(2));
                break;
            case "THIRTY_ENTRIES":
                subscription.setCountEntries(30);
                subscription.setPrice(BigDecimal.valueOf(38));
                subscription.setSubscriptionType(SubscriptionType.SIXTEEN_ENTRIES);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(2));
                break;
            case "ONE_MONTH":
                subscription.setCountEntries(30);
                subscription.setPrice(BigDecimal.valueOf(40));
                subscription.setSubscriptionType(SubscriptionType.ONE_MONTH);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
            case "THREE_MONTHS":
                subscription.setCountEntries(90);
                subscription.setPrice(BigDecimal.valueOf(110));
                subscription.setSubscriptionType(SubscriptionType.THREE_MONTHS);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(3));
                break;
            case "SIX_MONTHS":
                subscription.setCountEntries(180);
                subscription.setPrice(BigDecimal.valueOf(210));
                subscription.setSubscriptionType(SubscriptionType.SIX_MONTHS);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                break;
            case "ONE_YEAR":
                subscription.setCountEntries(365);
                subscription.setPrice(BigDecimal.valueOf(320));
                subscription.setSubscriptionType(SubscriptionType.ONE_YEAR);
                subscription.setStartDate(LocalDateTime.now());
                subscription.setEndDate(subscription.getStartDate().plusMonths(12));
                break;
        }
        return subscription;
    }

    private String setSubscriptionName(String subscriptionType) {
        String name = "";
        switch (subscriptionType) {
            case "SIX_ENTRIES":
                name = "6 ПОСЕЩЕНИЯ";
                break;
            case "EIGHT_ENTRIES":
                name = "8 ПОСЕЩЕНИЯ";
                break;
            case "TWELVE_ENTRIES":
                name = "12 ПОСЕЩЕНИЯ";
                break;
            case "SIXTEEN_ENTRIES":
                name = "16 ПОСЕЩЕНИЯ";
                break;
            case "TWENTY_FOUR_ENTRIES":
                name = "24 ПОСЕЩЕНИЯ";
                break;
            case "THIRTY_ENTRIES":
                name = "30 ПОСЕЩЕНИЯ";
                break;
            case "ONE_MONTH":
                name = "1 МЕСЕЦ";
                break;
            case "THREE_MONTHS":
                name = "3 МЕСЕЦА";
                break;
            case "SIX_MONTHS":
                name = "6 МЕСЕЦА";
                break;
            case "ONE_YEAR":
                name = "1 ГОДИНА";
                break;
        }
        return name;
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            Role rootAdmin = new Role();
            rootAdmin.setAuthority("ROOT_ADMIN");
            Role admin = new Role();
            admin.setAuthority("ADMIN");
            Role user = new Role();
            user.setAuthority("USER");
            this.roleRepository.save(rootAdmin);
            this.roleRepository.save(admin);
            this.roleRepository.save(user);
        }
    }

    private void seedRootAdmin() {
        if (userRepository.count() == 0) {
            User creator = new User();
            creator.setFirstName("Николай");
            creator.setLastName("Грозданов");
            creator.setSubscriptionNumber("00000001");
            creator.setAuthorities(new HashSet<>(this.roleRepository.findAll()));
            creator.setUsername("nike");
            creator.setPassword("$2a$10$SGyJV9GBu7eiap1aJQXel.po3HIu9rTSalbbZud6zZ3rEaMlci2wy");
            creator.setEmail("nagrozdanov@gmail.com");
            creator.setProfileImagePath("avatar.jpg");
            this.userRepository.save(creator);
        }
    }

    private void setUserRoles(User user, Boolean isAdmin) {
        Set<Role> authorities = new HashSet<>();
        Role roleUser = this.roleRepository.findByAuthority("USER");
        authorities.add(roleUser);
        if (isAdmin != null && isAdmin) {
            Role roleAdmin = this.roleRepository.findByAuthority("ADMIN");
            authorities.add(roleAdmin);
        }
        user.setAuthorities(authorities);
    }
    private User getUser(String userId) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (user == null){throw new ResourceNotFoundException(String.format(GlobalConstants.USER_WITH_SUCH_ID_DOESNT_EXISTS,userId));}
        return user;
    }
}
