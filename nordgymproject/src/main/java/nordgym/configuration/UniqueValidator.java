package nordgym.configuration;

import nordgym.annotation.UniqueUser;
import nordgym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueValidator implements ConstraintValidator<UniqueUser, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(String subscriptionNumber, ConstraintValidatorContext constraintValidatorContext) {
        return subscriptionNumber != null && userRepository.findBySubscriptionNumberIsLike(subscriptionNumber).isEmpty();
    }
}
