package com.example.paymentgateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionDataMapperDecryptTest {

    private TransactionDataMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new TransactionDataMapper();
    }

    @Test
    public void shouldDecryptCardholderName() {
        // given
        var transaction = buildTransaction();
        var encrypted = mapper.toEncrypted(transaction);

        // when
        var result = mapper.toDecrypted(encrypted);

        // then
        assertEquals(transaction.getCardholder().getName(), result.getCardholder().getName());
    }

    @Test
    public void shouldDecryptCardPAN() {
        // given
        var transaction = buildTransaction();
        var encrypted = mapper.toEncrypted(transaction);

        // when
        var result = mapper.toDecrypted(encrypted);

        // then
        assertEquals(transaction.getCard().getPan(), result.getCard().getPan());
    }

    @Test
    public void shouldDecryptCardExpiryDate() {
        // given
        var transaction = buildTransaction();
        var encrypted = mapper.toEncrypted(transaction);

        // when
        var result = mapper.toDecrypted(encrypted);

        // then
        assertEquals(transaction.getCard().getExpiry(), result.getCard().getExpiry());
    }

    @Test
    public void shouldEncryptAndDecryptTransaction() {
        // given
        var originalTransaction = buildTransaction();

        // when
        var encrypted = mapper.toEncrypted(originalTransaction);
        var decrypted = mapper.toDecrypted(encrypted);

        // then
        assertThat(originalTransaction)
                .usingRecursiveComparison()
                .ignoringFields("card.cvv")// as CVV was eliminated on encryption stage
                .isEqualTo(decrypted);
    }

}