package com.example.paymentgateway.web.rest;

import com.example.paymentgateway.domain.Transaction;
import com.example.paymentgateway.repository.TransactionRepository;
import com.example.paymentgateway.service.mapper.TransactionDataMapper;
import com.example.paymentgateway.web.rest.response.CreateTransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Map;

import static com.example.paymentgateway.testDataSupport.DataHelper.buildRandomTransaction;
import static com.example.paymentgateway.testDataSupport.DataHelper.nextRandomLong;
import static com.example.paymentgateway.web.rest.TransactionController.TRANSACTIONS_API_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository<Transaction> repository;

    @Autowired
    private TransactionDataMapper mapper;

    @Test
    public void shouldAnswerWithSuccessfulResponseBodyWhenCreateTransactionRequestGiven() {
        // given
        var transaction = buildRandomTransaction();
        var httpEntity = new HttpEntity<>(transaction);

        // when
        var httpResponse = restTemplate.exchange(TRANSACTIONS_API_URL, POST, httpEntity, CreateTransactionResponse.class);

        // then
        assertNotNull(httpResponse.getBody());
        assertTrue(httpResponse.getBody().isApproved());
    }

    @Test
    public void shouldAnswerHttp201WhenCreateTransactionRequestGiven() {
        // given
        var transaction = buildRandomTransaction();
        var httpEntity = new HttpEntity<>(transaction);

        // when
        var response = restTemplate.exchange(TRANSACTIONS_API_URL, POST, httpEntity, CreateTransactionResponse.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var createdResourceLocation = response.getHeaders().getLocation().toString();
        assertEquals(TRANSACTIONS_API_URL + "/" + transaction.getInvoice(), createdResourceLocation);
    }

    @Test
    public void shouldSaveEncryptedTransactionWhenCreateTransactionRequestGiven() {
        // given
        var initialSize = repository.size();
        var transaction = buildRandomTransaction();
        var httpEntity = new HttpEntity<>(transaction);

        var encryptedTransaction = mapper.toEncrypted(transaction);

        // when
        restTemplate.exchange(TRANSACTIONS_API_URL, POST, httpEntity, CreateTransactionResponse.class);

        // then
        assertEquals(initialSize + 1, repository.size());
        assertEquals(encryptedTransaction, repository.findById(transaction.getInvoice()));
    }

    @Test
    public void shouldAnswer400AndNotApprovedWhenInvalidCreateTransactionRequestGiven() {
        // given
        var negativeAmountTransaction = buildRandomTransaction().toBuilder()
                .amount(BigDecimal.valueOf(-25))
                .build();
        var httpEntity = new HttpEntity<>(negativeAmountTransaction);

        // when
        var response = restTemplate.exchange(TRANSACTIONS_API_URL, POST, httpEntity, CreateTransactionResponse.class);

        // then
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isApproved());
    }

    @Test
    public void shouldAnswerWithActualErrorMessageWhenInvalidCreateTransactionRequestGiven() {
        // given
        var negativeAmountTransaction = buildRandomTransaction().toBuilder()
                .amount(BigDecimal.valueOf(-25))
                .build();
        var httpEntity = new HttpEntity<>(negativeAmountTransaction);

        // when
        var httpResponse = restTemplate.exchange(TRANSACTIONS_API_URL, POST, httpEntity, CreateTransactionResponse.class);

        // then
        assertNotNull(httpResponse.getBody());

        Map<String, String> fieldsToErrors = httpResponse.getBody().getErrors();
        assertEquals(1, fieldsToErrors.size());

        assertNotNull(fieldsToErrors.get("amount"));
        assertEquals("Amount should be a positive number.", fieldsToErrors.get("amount"));
    }

    @Test
    public void shouldAnswer200WithMaskedTransactionWhenFindByIdRequestGiven() {
        // given
        Transaction encryptedTransaction = mapper.toEncrypted(buildRandomTransaction());
        var savedTransaction = repository.save(encryptedTransaction);
        var maskedTransaction = mapper.toMasked(mapper.toDecrypted(savedTransaction));

        // when
        var result = restTemplate.exchange(
                TRANSACTIONS_API_URL + "/" + savedTransaction.getInvoice(),
                GET,
                null,
                Transaction.class
        );

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(maskedTransaction, result.getBody());
    }

    @Test
    public void shouldAnswer404WhenTryingToFindUnstoredTransaction() {
        // given
        var nonExistedInvoice = nextRandomLong();

        // when
        var result = restTemplate.exchange(
                TRANSACTIONS_API_URL + "/" + nonExistedInvoice,
                GET,
                null,
                Transaction.class
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

}