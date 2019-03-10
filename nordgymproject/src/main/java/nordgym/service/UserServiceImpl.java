package nordgym.service;

import nordgym.domain.entities.*;
import nordgym.domain.enums.SubscriptionType;
import nordgym.domain.models.binding.UserUpdateBindingModel;
import nordgym.domain.models.service.UserServiceModel;
import nordgym.domain.models.view.UserViewModel;
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
        Subscription subscription = createSubscription(subscriptionType);
        Role role = this.roleRepository.findByAuthority("USER");
        Set<Role> authorities = new HashSet<>() {{
            add(role);
        }};
        user.setAuthorities(authorities);
        user.setSubscription(subscription);
        this.roleRepository.saveAll(authorities);
        this.subscriptionRepository.save(subscription);
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
        }
        return this.userRepository.save(user) != null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        return user != null ? this.modelMapper.map(user, UserServiceModel.class) : null;

    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);
        return user != null ? this.modelMapper.map(user, UserServiceModel.class) : null;
    }

    @Override
    public UserUpdateBindingModel getUserUpdateBindingModelByUserId(String userId) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        return user != null ? this.modelMapper.map(user, UserUpdateBindingModel.class) : null;
    }

    @Override
    public UserServiceModel getUserBySubscriptionNumber(String subscriptionNumber) {
        User user = this.userRepository.findBySubscriptionNumberIsLike(subscriptionNumber).orElse(null);
        return user != null ? this.modelMapper.map(user, UserServiceModel.class) : null;
    }

    @Override
    public void renewSubscription(String userId, String subscriptionType) {
        User user = this.userRepository.findById(Long.parseLong(userId)).get();
        this.expiredSubscriptionRepository.save(this.modelMapper.map(user.getSubscription(), ExpiredSubscription.class));
        long subscriptionId = user.getSubscription().getId();
        user.setSubscription(null);
        user = this.userRepository.save(user);
        this.subscriptionRepository.deleteById(subscriptionId);
        Subscription subscription = this.createSubscription(subscriptionType);
        subscription = this.subscriptionRepository.saveAndFlush(subscription);
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
    public void deleteUser(String userId) {
        User user = this.userRepository.findById(Long.parseLong(userId)).orElse(null);
        if (user != null) {
//            user.getSubscription().getUsers().remove(user);
            user.getEntries().forEach(this.userEntryRepository::delete);
            user.getExpiredSubscriptions().forEach(this.expiredSubscriptionRepository::delete);
            this.userRepository.delete(user);
            this.subscriptionRepository.deleteById(user.getSubscription().getId());
            if (user.getSolariumSubscription() != null) {
                this.solariumSubscriptionRepository.deleteById(user.getSolariumSubscription().getId());
            }
            System.out.println();
        }
    }

    @Override
    public UserViewModel createUserViewModel(UserServiceModel userServiceModel) {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.setId(userServiceModel.getId());
        userViewModel.setFullName(userServiceModel.getFirstName().concat(" ").concat(userServiceModel.getLastName()));
        userViewModel.setProfileImagePath(userServiceModel.getProfileImagePath());
        userViewModel.setSubscriptionNumber(userServiceModel.getSubscriptionNumber());
        userViewModel.setSubscription(this.setSubscriptionName(userServiceModel.getSubscription().getSubscriptionType().name()));
        userViewModel.setSubscriptionFrom(userServiceModel.getSubscription().getStartDate());
        userViewModel.setSubscriptionTo(userServiceModel.getSubscription().getEndDate());
        userViewModel.setEntriesLeft(userServiceModel.getSubscription().getCountEntries());
        userViewModel.setActive(userServiceModel.getSubscription().getExpired());
        userViewModel.setSolariumSubscription(userServiceModel.getSolariumSubscription() != null ? userServiceModel.getSolariumSubscription() : new SolariumSubscription());
        userViewModel.setEmail(userServiceModel.getEmail() != null ? userServiceModel.getEmail() : "No Info");
        if (userServiceModel.getEntries().size() > 0) {
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

    private int setCountOfEntries(String subscriptionType) {
        int count = 0;
        switch (subscriptionType) {
            case "SIX_ENTRIES":
                count = 6;
                break;
            case "EIGHT_ENTRIES":
                count = 8;
                break;
            case "TWELVE_ENTRIES":
                count = 12;
                break;
            case "SIXTEEN_ENTRIES":
                count = 16;
                break;
            case "TWENTY_FOUR_ENTRIES":
                count = 24;
                break;
            case "THIRTY_ENTRIES":
                count = 30;
                break;
            case "ONE_MONTH":
                count = 30;
                break;
            case "THREE_MONTHS":
                count = 90;
                break;
            case "SIX_MONTHS":
                count = 180;
                break;
            case "ONE_YEAR":
                count = 365;
                break;
        }
        return count;
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
}
