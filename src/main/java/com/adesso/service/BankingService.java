package com.adesso.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BankingService {

    private final Map<Long, Double> balances = new HashMap<>();

    private final Map<Long, List<Map<String, Object>>> transactions = new HashMap<>();

    public BankingService() {
        balances.put(12345678L, 1500.75);

        transactions.put(12345678L, new ArrayList<>(List.of(
                Map.of("transactionId", "TXN1001", "date", "2025-02-10", "amount", -100.00, "currency", "EUR", "description", "Pagamento bolletta"),
                Map.of("transactionId", "TXN1002", "date", "2025-02-15", "amount", 2000.00, "currency", "EUR", "description", "Stipendio")
        )));
    }

    public Map<String, Object> getBalance(Long accountId) {
        if (!balances.containsKey(accountId)) {
            return Map.of("error", "Account non trovato");
        }
        return Map.of(
                "accountId", accountId,
                "balance", Map.of(
                        "available", balances.get(accountId),
                        "currency", "EUR"
                )
        );
    }

    public List<Map<String, Object>> getTransactions(Long accountId) {
        return transactions.getOrDefault(accountId, new ArrayList<>());
    }

    public Map<String, Object> makeTransfer(Long accountId, Map<String, Object> transferRequest) {
        if (!balances.containsKey(accountId)) {
            return Map.of("transferId", "UNKNOWN", "status", "FAILED", "message", "Account non trovato");
        }

        double amount = (double) transferRequest.get("amount");
        double currentBalance = balances.get(accountId);

        if (currentBalance < amount) {
            return Map.of("transferId", "TRF123457", "status", "FAILED", "message", "Fondi insufficienti");
        }

        balances.put(accountId, currentBalance - amount);

        transactions.get(accountId).add(Map.of(
                "transactionId", UUID.randomUUID().toString(),
                "date", new Date().toString(),
                "amount", -amount,
                "currency", transferRequest.get("currency"),
                "description", transferRequest.get("description")
        ));

        return Map.of("transferId", "TRF123456", "status", "SUCCESS", "message", "Bonifico effettuato con successo");
    }
}

