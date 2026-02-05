package com.moneymanager.controller;

import com.moneymanager.dto.AccountDTO;
import com.moneymanager.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(
            @Valid @RequestBody AccountDTO accountDTO,
            @RequestHeader("X-User-Id") String userId) {
        log.info("POST /accounts - Creating account for user: {}", userId);
        
        AccountDTO created = accountService.createAccount(userId, accountDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account created successfully");
        response.put("data", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAccounts(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts - Fetching all accounts for user: {}", userId);
        
        List<AccountDTO> accounts = accountService.getAllAccounts(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Accounts retrieved successfully");
        response.put("data", accounts);
        response.put("count", accounts.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveAccounts(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts/active - Fetching active accounts for user: {}", userId);
        
        List<AccountDTO> accounts = accountService.getActiveAccounts(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active accounts retrieved successfully");
        response.put("data", accounts);
        response.put("count", accounts.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAccountById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts/{} - Fetching account for user: {}", id, userId);
        
        AccountDTO account = accountService.getAccountById(userId, id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account retrieved successfully");
        response.put("data", account);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAccount(
            @PathVariable String id,
            @Valid @RequestBody AccountDTO accountDTO,
            @RequestHeader("X-User-Id") String userId) {
        log.info("PUT /accounts/{} - Updating account for user: {}", id, userId);
        
        AccountDTO updated = accountService.updateAccount(userId, id, accountDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account updated successfully");
        response.put("data", updated);
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateAccount(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        log.info("PATCH /accounts/{}/deactivate - Deactivating account for user: {}", id, userId);
        
        accountService.deactivateAccount(userId, id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account deactivated successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-balance")
    public ResponseEntity<Map<String, Object>> getTotalBalance(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts/total-balance - Getting total balance for user: {}", userId);
        
        Double totalBalance = accountService.getTotalBalance(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Total balance retrieved successfully");
        response.put("totalBalance", totalBalance);
        
        return ResponseEntity.ok(response);
    }
}
