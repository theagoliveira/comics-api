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
@Constraint(validatedBy = UniqueCPFValidator.class)
@Documented
public @interface UniqueCPF {

    String message() default "CPF already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
