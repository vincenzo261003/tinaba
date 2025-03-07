package com.adesso.controller;

import com.adesso.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(bankingService.getBalance(accountId));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(bankingService.getTransactions(accountId));
    }

    @PostMapping("/accounts/{accountId}/transfers")
    public ResponseEntity<Map<String, Object>> makeTransfer(@PathVariable Long accountId,
                                                            @RequestBody Map<String, Object> transferRequest) {
        return ResponseEntity.ok(bankingService.makeTransfer(accountId, transferRequest));
    }
}

