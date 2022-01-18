package com.example.paymentgateway.repository;

import com.example.paymentgateway.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryTransactionRepository implements TransactionRepository<Transaction> {

    private static final ConcurrentMap<Long, Transaction> store = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        store.put(transaction.getInvoice(), transaction);
        return transaction;
    }

    @Override
    public Transaction findById(Long id) {
        return store.get(id);
    }

    @Override
    public int size() {
        return store.size();
    }
}
