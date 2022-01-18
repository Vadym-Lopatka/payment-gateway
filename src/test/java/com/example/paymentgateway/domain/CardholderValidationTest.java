package com.example.paymentgateway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildCardholder;
import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardholderValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldNotValidateNullableCardholderName() {
        // given
        var cardholder = buildCardholder().toBuilder().name(null).build();
        var request = buildTransaction().toBuilder().cardholder(cardholder).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Cardholder name is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateEmptyCardholderName() {
        // given
        var cardholder = buildCardholder().toBuilder().name("").build();
        var request = buildTransaction().toBuilder().cardholder(cardholder).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Cardholder name is required.", violationMsg);
    }

    @Test
    public void shouldNotAcceptInvalidCardholderEmail() {
        // given
        var cardholder = buildCardholder().toBuilder().email("@null.123").build();
        var request = buildTransaction().toBuilder().cardholder(cardholder).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Invalid cardholder email format.", violationMsg);
    }

    @Test
    public void shouldNotValidateEmptyCardholderEmail() {
        // given
        var cardholder = buildCardholder().toBuilder().email("").build();
        var request = buildTransaction().toBuilder().cardholder(cardholder).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Invalid cardholder email format.", violationMsg);
    }

    @Test
    public void shouldNotValidateNullableCardholderEmail() {
        // given
        var cardholder = buildCardholder().toBuilder().email(null).build();
        var request = buildTransaction().toBuilder().cardholder(cardholder).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Invalid cardholder email format.", violationMsg);
    }

}