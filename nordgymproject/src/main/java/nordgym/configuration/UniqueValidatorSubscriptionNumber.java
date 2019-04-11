package nordgym.configuration;

import nordgym.annotation.UniqueSubscriptionNumber;
import nordgym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueValidatorSubscriptionNumber implements ConstraintValidator<UniqueSubscriptionNumber, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueValidatorSubscriptionNumber(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueSubscriptionNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String subscriptionNumber, ConstraintValidatorContext constraintValidatorContext) {
        return subscriptionNumber != null && userRepository.findBySubscriptionNumberIsLike(subscriptionNumber).isEmpty();
    }

}
