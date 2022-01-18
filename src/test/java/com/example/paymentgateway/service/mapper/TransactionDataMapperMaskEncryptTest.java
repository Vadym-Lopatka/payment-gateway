package com.example.paymentgateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionDataMapperMaskEncryptTest {

    private TransactionDataMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new TransactionDataMapper();
    }

    @Test
    public void shouldEncryptCardholderName() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toEncrypted(transaction);

        // then
        assertNotEquals(transaction.getCardholder().getName(), result.getCardholder().getName());
    }

    @Test
    public void shouldEncryptCardPAN() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toEncrypted(transaction);

        // then
        assertNotEquals(transaction.getCard().getPan(), result.getCard().getPan());
    }

    @Test
    public void shouldEncryptCardExpiryDate() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toEncrypted(transaction);

        // then
        assertNotEquals(transaction.getCard().getExpiry(), result.getCard().getExpiry());
    }

    @Test
    public void shouldEliminateCardCVVWhenEncrypt() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toEncrypted(transaction);

        // then
        assertNotEquals(transaction.getCard().getCvv(), result.getCard().getCvv());
        assertNull(result.getCard().getCvv());
    }

    @Test
    public void shouldVerifyThatEncryptedTransactionIsNotEqualToOriginal() {
        // given
        var originalTransaction = buildTransaction();

        // when
        var encrypted = mapper.toEncrypted(originalTransaction);

        // then
        assertNotEquals(originalTransaction, encrypted);
    }
}