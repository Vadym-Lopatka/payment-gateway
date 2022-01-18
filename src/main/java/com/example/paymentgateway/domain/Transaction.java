package com.example.paymentgateway.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class Transaction {
    @NotNull(message = "Invoice is required.")
    private final Long invoice;

    @NotNull(message = "Amount is required.")
    @Positive(message = "Amount should be a positive number.")
    private final BigDecimal amount;

    @NotEmpty(message = "Currency is required.")
    private final String currency;

    @Valid
    @NotNull(message = "Cardholder is required.")
    private final Cardholder cardholder;

    @Valid
    @NotNull(message = "Card is required.")
    private final Card card;
}
