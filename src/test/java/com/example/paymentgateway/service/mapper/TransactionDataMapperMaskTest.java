package com.example.paymentgateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.*;

class TransactionDataMapperMaskTest {

    private TransactionDataMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new TransactionDataMapper();
    }

    @Test
    public void shouldMaskCardholderName() {
        // given
        var transaction = buildTransaction();
        String cardholderName = transaction.getCardholder().getName();

        // when
        var result = mapper.toMasked(transaction);

        // then
        assertNotEquals(cardholderName, result.getCardholder().getName());
        assertEquals(mask(cardholderName), result.getCardholder().getName());
    }

    private String mask(String cardholderName) {
        return "*".repeat(cardholderName.length());
    }

    @Test
    public void shouldProveThatMaskedPANIsNotEqualToOriginal() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toMasked(transaction);

        // then
        assertNotEquals(transaction.getCard().getPan(), result.getCard().getPan());
    }

    @Test
    public void shouldProveThatMaskedPANHasSameSizeAsOriginal() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toMasked(transaction);

        // then
        assertEquals(transaction.getCard().getPan().length(), result.getCard().getPan().length());
    }

    @Test
    public void shouldLeaveLast4SymbolsOfMaskedPANEqualToOriginalNumbers() {
        // given
        var transaction = buildTransaction();
        String original = transaction.getCard().getPan();
        String originalLast4Numbers = transaction.getCard().getPan().substring(original.length() - 4);

        // when
        var result = mapper.toMasked(transaction);

        // then
        String maskedPan = result.getCard().getPan();
        String maskedPanLast4Numbers = maskedPan.substring(maskedPan.length() - 4);
        assertEquals(originalLast4Numbers, maskedPanLast4Numbers);
    }

    @Test
    public void shouldMaskedAllPANButNotLast4Numbers() {
        // given
        var transaction = buildTransaction();
        var originalPan = transaction.getCard().getPan();

        // when
        var result = mapper.toMasked(transaction);

        // then
        var maskedPan = result.getCard().getPan();

        assertEquals(
                mask(originalPan.substring(0, originalPan.length() - 4)),
                maskedPan.substring(0, maskedPan.length() - 4)
        );
    }

    @Test
    public void shouldMaskCardExpiryDate() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toMasked(transaction);

        // then
        assertEquals(mask(transaction.getCard().getExpiry()), result.getCard().getExpiry());
    }

    @Test
    public void shouldEliminateCardCVVWhenMask() {
        // given
        var transaction = buildTransaction();

        // when
        var result = mapper.toMasked(transaction);

        // then
        assertNotEquals(transaction.getCard().getCvv(), result.getCard().getCvv());
        assertNull(result.getCard().getCvv());
    }

    @Test
    public void shouldVerifyThatMaskedTransactionIsNotEqualToOriginal() {
        // given
        var originalTransaction = buildTransaction();

        // when
        var result = mapper.toMasked(originalTransaction);

        // then
        assertNotEquals(originalTransaction, result);
    }

}