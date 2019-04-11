package nordgym.annotation;
import nordgym.configuration.UniqueValidatorUsername;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidatorUsername.class)
public @interface UniqueUsername {
    String message() default "Must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
