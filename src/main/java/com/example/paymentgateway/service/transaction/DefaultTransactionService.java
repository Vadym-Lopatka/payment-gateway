package com.example.paymentgateway.service.transaction;

import com.example.paymentgateway.domain.Transaction;
import com.example.paymentgateway.repository.TransactionRepository;
import com.example.paymentgateway.service.mapper.TransactionDataMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultTransactionService implements TransactionService {

    private final TransactionRepository<Transaction> repository;
    private final ApplicationEventPublisher auditEventsPublisher;
    private final TransactionDataMapper dataMapper;

    public DefaultTransactionService(
            TransactionRepository<Transaction> repository,
            ApplicationEventPublisher auditEventsPublisher,
            TransactionDataMapper dataMapper
    ) {
        this.repository = repository;
        this.auditEventsPublisher = auditEventsPublisher;
        this.dataMapper = dataMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        Transaction encrypted = dataMapper.toEncrypted(transaction);
        repository.save(encrypted);
        auditEventsPublisher.publishEvent(dataMapper.toMasked(transaction));

        return encrypted;
    }

    @Override
    public Optional<Transaction> findOne(Long invoice) {
        return Optional.ofNullable(repository.findById(invoice))
                .map(dataMapper::toDecrypted)
                .map(dataMapper::toMasked);
    }
}
