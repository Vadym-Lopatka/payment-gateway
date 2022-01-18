package com.example.paymentgateway.service.mapper;

import com.example.paymentgateway.domain.Card;
import com.example.paymentgateway.domain.Cardholder;
import com.example.paymentgateway.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.function.UnaryOperator;

@Service
public class TransactionDataMapper {

    public Transaction toEncrypted(Transaction transaction) {
        return map(transaction, s -> Base64.getEncoder().encodeToString(s.getBytes()));
    }

    public Transaction toDecrypted(Transaction transaction) {
        return map(transaction, s -> new String(Base64.getDecoder().decode(s)));
    }

    public Transaction toMasked(Transaction transaction) {
        var maskedTransaction = map(transaction, s -> "*".repeat(s.length()));
        var maskedCard = replaceLast4PanNumbers(maskedTransaction.getCard(), transaction.getCard().getPan());

        return maskedTransaction.toBuilder()
                .card(maskedCard)
                .build();
    }

    private Card replaceLast4PanNumbers(Card maskedCard, String originalPan) {
        var last4OriginalNumbers = originalPan.substring(originalPan.length() - 4);
        var maskedPanWithLast4OriginalNumbers = maskedCard.getPan().substring(0, originalPan.length() - 4) + last4OriginalNumbers;

        return maskedCard.toBuilder()
                .pan(maskedPanWithLast4OriginalNumbers)
                .build();
    }

    private Transaction map(Transaction transaction, UnaryOperator<String> operator) {
        var mappedCardholder = map(transaction.getCardholder(), operator);
        var mappedCard = map(transaction.getCard(), operator);

        return transaction.toBuilder()
                .cardholder(mappedCardholder)
                .card(mappedCard)
                .build();
    }

    private Cardholder map(Cardholder cardholder, UnaryOperator<String> operator) {
        return cardholder.toBuilder()
                .name(operator.apply(cardholder.getName()))
                .build();
    }

    private Card map(Card card, UnaryOperator<String> operator) {
        return card.toBuilder()
                .pan(operator.apply(card.getPan()))
                .expiry(operator.apply(card.getExpiry()))
                .cvv(null)
                .build();
    }
}
