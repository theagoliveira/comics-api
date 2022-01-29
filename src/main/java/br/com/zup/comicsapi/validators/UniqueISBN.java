package br.com.zup.comicsapi.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueISBNValidator.class)
@Documented
public @interface UniqueISBN {

    String message() default "ISBN already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
