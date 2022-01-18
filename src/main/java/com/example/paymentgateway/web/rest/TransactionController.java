package com.example.paymentgateway.web.rest;


import com.example.paymentgateway.domain.Transaction;
import com.example.paymentgateway.service.transaction.TransactionService;
import com.example.paymentgateway.web.rest.response.CreateTransactionResponse;
import com.example.paymentgateway.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(TransactionController.TRANSACTIONS_API_URL)
public class TransactionController {
    protected static final String TRANSACTIONS_API_URL = "/api/transactions";
    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateTransactionResponse> create(@Valid @RequestBody Transaction transaction) {
        log.debug("REST request to save Transaction with invoice: {}", transaction.getInvoice());

        Transaction result = transactionService.save(transaction);
        return ResponseEntity
                .created(URI.create("/api/transactions/" + result.getInvoice()))
                .body(CreateTransactionResponse.builder().approved(true).build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable("id") Long id) {
        log.debug("REST request to get Transaction by invoice : {}", id);

        var result = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }
}

