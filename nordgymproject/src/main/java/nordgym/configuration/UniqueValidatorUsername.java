package nordgym.configuration;
import nordgym.annotation.UniqueUsername;
import nordgym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UniqueValidatorUsername implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueValidatorUsername(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return username != null && userRepository.findByUsername(username).isEmpty();
    }

}
