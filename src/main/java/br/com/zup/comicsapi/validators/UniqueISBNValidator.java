package br.com.zup.comicsapi.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.zup.comicsapi.repositories.ComicRepository;

public class UniqueISBNValidator implements ConstraintValidator<UniqueISBN, String> {

    private final ComicRepository comicRepository;

    public UniqueISBNValidator(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return !comicRepository.existsByIsbn(value.replaceAll("[^0-9]", ""));
    }

}
