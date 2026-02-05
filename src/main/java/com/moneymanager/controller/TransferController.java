package com.moneymanager.controller;

import com.moneymanager.dto.TransferDTO;
import com.moneymanager.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts/transfer")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTransfer(
            @Valid @RequestBody TransferDTO transferDTO,
            @RequestHeader("X-User-Id") String userId) {
        log.info("POST /accounts/transfer - Creating transfer for user: {}", userId);
        
        TransferDTO created = transferService.createTransfer(userId, transferDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transfer completed successfully");
        response.put("data", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTransfersByUser(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts/transfer - Fetching transfers for user: {}", userId);
        
        List<TransferDTO> transfers = transferService.getTransfersByUser(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transfers retrieved successfully");
        response.put("data", transfers);
        response.put("count", transfers.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Map<String, Object>> getTransfersByAccount(
            @PathVariable String accountId) {
        log.info("GET /accounts/transfer/account/{} - Fetching transfers for account: {}", accountId, accountId);
        
        List<TransferDTO> transfers = transferService.getTransfersByAccount(accountId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transfers by account retrieved successfully");
        response.put("data", transfers);
        response.put("count", transfers.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/date-range")
    public ResponseEntity<Map<String, Object>> getTransfersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /accounts/transfer/filter/date-range - Fetching transfers between {} and {} for user: {}", 
                startDate, endDate, userId);
        
        List<TransferDTO> transfers = transferService.getTransfersByDateRange(userId, startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Transfers by date range retrieved successfully");
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("data", transfers);
        response.put("count", transfers.size());
        
        return ResponseEntity.ok(response);
    }
}
