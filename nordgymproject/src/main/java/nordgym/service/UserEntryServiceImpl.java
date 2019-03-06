package nordgym.service;

import nordgym.domain.entities.ExpiredSubscription;
import nordgym.domain.entities.User;
import nordgym.domain.entities.UserEntry;
import nordgym.repository.ExpiredSubscriptionRepository;
import nordgym.repository.UserEntryRepository;
import nordgym.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserEntryServiceImpl implements UserEntryService {
    private final UserEntryRepository userEntryRepository;
    private final UserRepository userRepository;
    private final ExpiredSubscriptionRepository expiredSubscriptionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserEntryServiceImpl(UserEntryRepository userEntryRepository, UserRepository userRepository, ExpiredSubscriptionRepository expiredSubscriptionRepository, ModelMapper modelMapper) {
        this.userEntryRepository = userEntryRepository;
        this.userRepository = userRepository;
        this.expiredSubscriptionRepository = expiredSubscriptionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean checkInUser(Long userId) {

        User userEntity = this.userRepository.findById(userId).orElse(null);
        if (userEntity != null && userEntity.getSubscription().getCountEntries() > 0) {
            UserEntry userEntry = new UserEntry();
            userEntry.setDateAndTimeOfUserEntry(LocalDateTime.now());
            userEntry = this.userEntryRepository.saveAndFlush(userEntry);
            userEntry.getUsers().add(userEntity);
            userEntity.getEntries().add(userEntry);
            userEntity.getSubscription().setCountEntries(userEntity.getSubscription().getCountEntries() - 1);
            if (userEntity.getSubscription().getCountEntries() < 1){
                ExpiredSubscription expiredSubscription = this.modelMapper.map(userEntity.getSubscription(),ExpiredSubscription.class);
                this.expiredSubscriptionRepository.save(expiredSubscription);
            }
            this.userRepository.save(userEntity);
        }
        return this.userRepository.saveAndFlush(userEntity) != null;
    }

    @Override
    public boolean removeLastEntry(Long entryId,Long userId) {

        User user = this.userRepository.findById(userId).orElse(null);
        UserEntry userEntry = userEntryRepository.findById(entryId).orElse(null);
        if (user != null && !user.getEntries().isEmpty()) {

            user.getEntries().remove(userEntry);
            user.getSubscription().setCountEntries(user.getSubscription().getCountEntries()+1);
            this.userRepository.save(user);
            this.userEntryRepository.delete(userEntry);
        }
        return this.userRepository.save(user) != null;
    }
}
