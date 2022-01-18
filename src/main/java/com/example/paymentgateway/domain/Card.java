package com.example.paymentgateway.domain;

import com.example.paymentgateway.web.constraint.NotExpired;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.LuhnCheck;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class Card {
    @NotNull(message = "Pan is required.")
    @LuhnCheck(startIndex = 0, endIndex = 15, message = "Pan is invalid.")
    private final String pan;

    @NotNull(message = "Expiry is required.")
    @NotExpired(inputFormat = "MMyy", message = "Payment card is expired.")
    private final String expiry;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "CVV is required.")
    @Size(min = 3, max = 3, message = "Invalid CVV format.")
    private final String cvv;
}
