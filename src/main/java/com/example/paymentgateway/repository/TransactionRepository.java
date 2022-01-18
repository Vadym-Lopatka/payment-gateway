package com.example.paymentgateway.repository;

public interface TransactionRepository<S> {
    S save(S dto);

    S findById(Long id);

    int size();
}
