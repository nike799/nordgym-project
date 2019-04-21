package com.nordgym.service;

import com.nordgym.constants.GlobalConstants;
import com.nordgym.errors.ResourceNotFoundException;
import com.nordgym.repository.ExpiredSubscriptionRepository;
import com.nordgym.repository.UserEntryRepository;
import com.nordgym.repository.UserRepository;
import com.nordgym.domain.entities.User;
import com.nordgym.domain.entities.UserEntry;
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
    public boolean checkInUser(Long id) {
        User user = getUser(id);
        if (user.getSubscription().getCountEntries() > 0) {
            UserEntry userEntry = new UserEntry();
            userEntry.setDateAndTimeOfUserEntry(LocalDateTime.now());
            userEntry = this.userEntryRepository.saveAndFlush(userEntry);
            userEntry.getUsers().add(user);
            user.getEntries().add(userEntry);
            user.getSubscription().setCountEntries(user.getSubscription().getCountEntries() - 1);
            this.userRepository.save(user);
        }
        return this.userRepository.saveAndFlush(user) != null;
    }

    @Override
    public boolean removeLastEntry(Long entryId, Long userId) {
        User user = getUser(userId);
        UserEntry userEntry = userEntryRepository.findById(entryId).orElseThrow(
                ()-> new ResourceNotFoundException(String.format(GlobalConstants.USER_ENTRY_WITH_SUCH_ID_DOESNT_EXISTS,entryId)));
        if (!user.getEntries().isEmpty()) {
            user.getEntries().remove(userEntry);
            user.getSubscription().setCountEntries(user.getSubscription().getCountEntries() + 1);
            this.userRepository.save(user);
            this.userEntryRepository.delete(userEntry);
        }
        return this.userRepository.save(user) != null;
    }
    private User getUser(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(GlobalConstants.USER_WITH_SUCH_ID_DOESNT_EXISTS, id));
        }
        return user;
    }
}
