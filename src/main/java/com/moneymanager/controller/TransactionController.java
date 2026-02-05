package com.moneymanager.controller;

import com.moneymanager.dto.TransactionDTO;
import com.moneymanager.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestHeader("X-User-Id") String userId) {
        log.info("POST /transactions - Creating transaction for user: {}", userId);
        
        TransactionDTO created = transactionService.createTransaction(userId, transactionDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transaction created successfully");
        response.put("data", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTransactions(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions - Fetching all transactions for user: {}", userId);
        
        List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transactions retrieved successfully");
        response.put("data", transactions);
        response.put("count", transactions.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTransactionById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions/{} - Fetching transaction for user: {}", id, userId);
        
        TransactionDTO transaction = transactionService.getTransactionById(userId, id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transaction retrieved successfully");
        response.put("data", transaction);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Map<String, Object>> getTransactionsByType(
            @PathVariable String type,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions/type/{} - Fetching {} transactions for user: {}", type, type, userId);
        
        List<TransactionDTO> transactions = transactionService.getTransactionsByType(userId, type);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", type + " transactions retrieved successfully");
        response.put("data", transactions);
        response.put("count", transactions.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getTransactionsByCategory(
            @PathVariable String category,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions/category/{} - Fetching transactions for user: {}", category, userId);
        
        List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(userId, category);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transactions by category retrieved successfully");
        response.put("category", category);
        response.put("data", transactions);
        response.put("count", transactions.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/division/{division}")
    public ResponseEntity<Map<String, Object>> getTransactionsByDivision(
            @PathVariable String division,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions/division/{} - Fetching transactions for user: {}", division, userId);
        
        List<TransactionDTO> transactions = transactionService.getTransactionsByDivision(userId, division);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transactions by division retrieved successfully");
        response.put("division", division);
        response.put("data", transactions);
        response.put("count", transactions.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/date-range")
    public ResponseEntity<Map<String, Object>> getTransactionsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /transactions/filter/date-range - Fetching transactions between {} and {} for user: {}", 
                startDate, endDate, userId);
        
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transactions by date range retrieved successfully");
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("data", transactions);
        response.put("count", transactions.size());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTransaction(
            @PathVariable String id,
            @Valid @RequestBody TransactionDTO transactionDTO,
            @RequestHeader("X-User-Id") String userId) {
        log.info("PUT /transactions/{} - Updating transaction for user: {}", id, userId);
        
        TransactionDTO updated = transactionService.updateTransaction(userId, id, transactionDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transaction updated successfully");
        response.put("data", updated);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTransaction(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        log.info("DELETE /transactions/{} - Deleting transaction for user: {}", id, userId);
        
        transactionService.deleteTransaction(userId, id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transaction deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
