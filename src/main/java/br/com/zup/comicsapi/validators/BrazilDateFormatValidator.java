package br.com.zup.comicsapi.validators;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BrazilDateFormatValidator implements ConstraintValidator<BrazilDateFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[0-9]{2}/[0-9]{2}/[0-9]{4}$");
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches()) {
            return false;
        } else {
            int maxAge = 150;
            int day = Integer.parseInt(value.substring(0, 2));
            int month = Integer.parseInt(value.substring(3, 5));
            int year = Integer.parseInt(value.substring(6, 10));
            LocalDate date;
            LocalDate today = LocalDate.now();

            // This initialization runs some verifications (e.g. negative values, leap year)
            // and throws a DateTimeException if the date is not valid.
            try {
                date = LocalDate.of(year, month, day);
            } catch (DateTimeException e) {
                return false;
            }

            return !date.isAfter(today) && !date.isBefore(today.minusYears(maxAge));
        }
    }

}
