package com.example.paymentgateway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildCard;
import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldNotValidateNullableCardPan() {
        // given
        var card = buildCard().toBuilder().pan(null).build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Pan is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateInvalidSizeOfCardPan() {
        // given
        var card = buildCard().toBuilder().pan("123").build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Pan is invalid.", violationMsg);
    }

    @Test
    public void shouldNotValidateWhenLuhnCheckIsNotPassedOnCardPan() {
        // given
        var card = buildCard().toBuilder().pan("123").build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Pan is invalid.", violationMsg);
    }

    @Test
    public void shouldNotValidateWhenCvvIsNullable() {
        // given
        var card = buildCard().toBuilder().cvv(null).build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("CVV is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateWhenCvvIsInvalid() {
        // given
        var card = buildCard().toBuilder().cvv("9").build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Invalid CVV format.", violationMsg);
    }

    @Test
    public void shouldNotValidateWhenExpiryIsNullable() {
        // given
        var card = buildCard().toBuilder().expiry(null).build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Expiry is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateWhenExpiryIsOutdated() {
        // given
        var card = buildCard().toBuilder().expiry("0921").build();
        var request = buildTransaction().toBuilder().card(card).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Payment card is expired.", violationMsg);
    }
}