package com.example.paymentgateway.service.audit;

import com.example.paymentgateway.domain.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionAuditEventsListener {
    private static final Logger audit = LoggerFactory.getLogger("audit-log");

    private final ObjectMapper objectMapper;

    public TransactionAuditEventsListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @EventListener
    @SneakyThrows
    public void handler(Transaction transaction) {
        audit.info(objectMapper.writeValueAsString(transaction));
    }

}
