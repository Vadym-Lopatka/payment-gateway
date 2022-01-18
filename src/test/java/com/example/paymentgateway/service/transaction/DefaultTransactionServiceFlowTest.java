package com.example.paymentgateway.service.transaction;

import com.example.paymentgateway.domain.Transaction;
import com.example.paymentgateway.repository.TransactionRepository;
import com.example.paymentgateway.service.mapper.TransactionDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildRandomTransaction;
import static com.example.paymentgateway.testDataSupport.DataHelper.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTransactionServiceFlowTest {

    private DefaultTransactionService transactionService;

    @Mock
    private TransactionRepository<Transaction> repository;

    @Mock
    private TransactionDataMapper dataMapper;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    public void setup() {
        transactionService = new DefaultTransactionService(repository, publisher, dataMapper);
    }

    @Test
    public void shouldStoreEncryptedTransactionWhenSaving() {
        // given
        var transaction = buildRandomTransaction();
        Transaction encryptedTransactionStub = buildEncryptedTransactionStub(transaction);

        when(dataMapper.toEncrypted(transaction)).thenReturn(encryptedTransactionStub);

        // when
        var result = transactionService.save(transaction);

        // then
        verify(dataMapper, times(1)).toEncrypted(transaction);
        verify(repository, times(1)).save(encryptedTransactionStub);
    }

    @Test
    public void shouldReturnEncryptedTransactionWhenSaving() {
        // given
        var transaction = buildRandomTransaction();
        Transaction encryptedTransactionStub = buildEncryptedTransactionStub(transaction);

        when(dataMapper.toEncrypted(transaction)).thenReturn(encryptedTransactionStub);
        when(repository.save(encryptedTransactionStub)).thenReturn(encryptedTransactionStub);

        // when
        var result = transactionService.save(transaction);

        // then
        assertEquals(encryptedTransactionStub, result);
    }

    @Test
    public void shouldDecryptAndMaskTransactionBeforeReturningWhenFindOne() {
        // given
        Long invoice = 1L;
        var decryptedTransaction = buildTransaction(invoice);

        var storedEncryptedTransaction = buildEncryptedTransactionStub(decryptedTransaction);
        when(repository.findById(invoice)).thenReturn(storedEncryptedTransaction);

        when(dataMapper.toDecrypted(storedEncryptedTransaction)).thenReturn(decryptedTransaction);

        Transaction maskedTransactionStub = buildMaskedTransactionStub(decryptedTransaction);
        when(dataMapper.toMasked(decryptedTransaction)).thenReturn(maskedTransactionStub);

        // when
        Optional<Transaction> result = transactionService.findOne(invoice);

        // then
        verify(dataMapper, times(1)).toDecrypted(storedEncryptedTransaction);
        verify(dataMapper, times(1)).toMasked(decryptedTransaction);

        // and
        assertTrue(result.isPresent());
        assertEquals(maskedTransactionStub, result.get());
    }

    @Test
    public void shouldReturnMaskedTransactionWhenCallingFindOne() {
        // given
        Long invoice = 1L;
        var decryptedTransaction = buildTransaction(invoice);

        var storedEncryptedTransaction = buildEncryptedTransactionStub(decryptedTransaction);
        when(repository.findById(invoice)).thenReturn(storedEncryptedTransaction);

        when(dataMapper.toDecrypted(storedEncryptedTransaction)).thenReturn(decryptedTransaction);

        Transaction maskedTransactionStub = buildMaskedTransactionStub(decryptedTransaction);
        when(dataMapper.toMasked(decryptedTransaction)).thenReturn(maskedTransactionStub);

        // when
        Optional<Transaction> result = transactionService.findOne(invoice);

        // then
        assertTrue(result.isPresent());
        assertEquals(maskedTransactionStub, result.get());
    }

    private Transaction buildEncryptedTransactionStub(Transaction transaction) {
        var encryptedCardholder = transaction.getCardholder().toBuilder().name("base64EncryptedVal").build();
        return transaction.toBuilder().cardholder(encryptedCardholder).build();
    }

    private Transaction buildMaskedTransactionStub(Transaction transaction) {
        var encryptedCardholder = transaction.getCardholder().toBuilder().name("**********").build();
        return transaction.toBuilder().cardholder(encryptedCardholder).build();
    }
}