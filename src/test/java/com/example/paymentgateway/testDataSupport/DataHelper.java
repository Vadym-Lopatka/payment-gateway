package com.example.paymentgateway.testDataSupport;


import com.example.paymentgateway.domain.Card;
import com.example.paymentgateway.domain.Cardholder;
import com.example.paymentgateway.domain.Transaction;

import java.math.BigDecimal;
import java.util.Random;

public class DataHelper {

    private static final Random random = new Random();

    public static Transaction buildRandomTransaction() {
        return buildTransaction(nextRandomLong());
    }

    public static Transaction buildTransaction() {
        return buildTransaction(123456L);
    }

    public static Transaction buildTransaction(Long invoice) {
        return Transaction.builder()
                .invoice(invoice)
                .amount(BigDecimal.TEN)
                .currency("EUR")
                .cardholder(buildCardholder())
                .card(buildCard())
                .build();
    }

    public static Card buildCard() {
        return Card.builder()
                .pan("1802909582961827")
                .expiry("0624")
                .cvv("789")
                .build();
    }

    public static Cardholder buildCardholder() {
        return Cardholder.builder()
                .name("Test User")
                .email("testuser@web.com")
                .build();
    }

    public static long nextRandomLong() {
        return random.nextLong();
    }
}
