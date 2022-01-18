package com.example.paymentgateway.web.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class CreateTransactionResponse {

    private final boolean approved;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, String> errors;
}
