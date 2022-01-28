package br.com.zup.comicsapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.comicsapi.repositories.UserRepository;

public class UniqueCPFValidator implements ConstraintValidator<UniqueCPF, String> {

    private final UserRepository userRepository;

    public UniqueCPFValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return !userRepository.existsByCpf(value.replaceAll("[^0-9]", ""));
    }

}
