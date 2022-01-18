package com.example.paymentgateway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldNotValidateNullableInvoice() {
        // given
        var request = buildTransaction().toBuilder().invoice(null).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Invoice is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateNullableAmount() {
        // given
        var request = buildTransaction().toBuilder().amount(null).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Amount is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateNegativeAmount() {
        // given
        var request = buildTransaction().toBuilder().amount(BigDecimal.valueOf(-12)).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Amount should be a positive number.", violationMsg);
    }

    @Test
    public void shouldNotValidateNullableCurrency() {
        // given
        var request = buildTransaction().toBuilder().currency(null).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Currency is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateEmptyCurrency() {
        // given
        var request = buildTransaction().toBuilder().currency("").build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Currency is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateNullableCardholder() {
        // given
        var request = buildTransaction().toBuilder().cardholder(null).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Cardholder is required.", violationMsg);
    }

    @Test
    public void shouldNotValidateNullableCard() {
        // given
        var request = buildTransaction().toBuilder().card(null).build();

        // when
        var violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
        var violationMsg = violations.iterator().next().getMessage();
        assertEquals("Card is required.", violationMsg);
    }

}