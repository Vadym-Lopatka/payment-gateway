package com.example.paymentgateway.service.transaction;

import com.example.paymentgateway.domain.Transaction;

import java.util.Optional;

public interface TransactionService {
    Transaction save(Transaction transaction);

    Optional<Transaction> findOne(Long id);
}
