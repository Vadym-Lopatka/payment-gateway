package com.example.paymentgateway.web.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class NotExpiredValidator implements ConstraintValidator<NotExpired, String> {

    protected String inputFormat;

    @Override
    public void initialize(NotExpired notExpired) {
        this.inputFormat = notExpired.inputFormat();
    }

    @Override
    public boolean isValid(String expiryValue, ConstraintValidatorContext constraintValidatorContext) {
        // null values are valid
        if (expiryValue == null) {
            return true;
        }

        try {
            DateTimeFormatter format = new DateTimeFormatterBuilder()
                    .appendPattern(inputFormat)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();

            var expirationMonthYear = LocalDate.parse(expiryValue, format);
            var lastExpirationDay = YearMonth.from(expirationMonthYear).atEndOfMonth();

            return lastExpirationDay.isAfter(LocalDate.now());
        } catch (java.time.DateTimeException e) {
            return false;
        }
    }
}
