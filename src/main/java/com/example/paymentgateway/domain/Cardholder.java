package com.example.paymentgateway.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class Cardholder {

    @NotEmpty(message = "Cardholder name is required.")
    private final String name;


    @Email(message = "Invalid cardholder email format.")
    @NotNull(message = "Invalid cardholder email format.")
    @Size(min = 5, max = 254, message = "Invalid cardholder email format.")
    private final String email;
}
