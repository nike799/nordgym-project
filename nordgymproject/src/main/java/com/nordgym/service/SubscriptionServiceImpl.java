package com.nordgym.service;

import com.nordgym.repository.ExpiredSubscriptionRepository;
import com.nordgym.repository.SubscriptionRepository;
import com.nordgym.repository.UserRepository;
import com.nordgym.domain.entities.ExpiredSubscription;
import com.nordgym.domain.entities.Subscription;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ExpiredSubscriptionRepository expiredSubscriptionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                   ExpiredSubscriptionRepository expiredSubscriptionRepository,
                                   UserRepository userRepository, ModelMapper modelMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.expiredSubscriptionRepository = expiredSubscriptionRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Scheduled(cron = "0 32 20 * * *")
    void removeExpiredSubscriptions() {
        List<Subscription> subscriptions = this.subscriptionRepository.findAllByEndDateIsBefore(LocalDateTime.now()).orElse(null);
        if (subscriptions != null) {
            List<ExpiredSubscription> expiredSubscriptions = List.of(this.modelMapper.map(subscriptions.toArray(), ExpiredSubscription[].class));
            this.expiredSubscriptionRepository.saveAll(expiredSubscriptions);
            subscriptions.forEach(subscription -> subscription.getUsers().forEach(user -> {
                user.setSubscription(null);
                this.userRepository.save(user);
            }));
            this.subscriptionRepository.deleteAll(subscriptions);
        }
    }
}
