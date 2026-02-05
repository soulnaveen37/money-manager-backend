package com.moneymanager.service;

import com.moneymanager.dto.TransactionDTO;
import com.moneymanager.model.Transaction;
import com.moneymanager.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Value("${app.transaction.edit-limit-hours:12}")
    private long editLimitHours;

    public TransactionDTO createTransaction(String userId, TransactionDTO transactionDTO) {
        log.info("Creating transaction for user: {}", userId);
        
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUserId(userId);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setDeleted(false);
        transaction.setStatus("COMPLETED");

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully: {}", savedTransaction.getId());
        
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    public TransactionDTO getTransactionById(String userId, String transactionId) {
        log.info("Fetching transaction: {} for user: {}", transactionId, userId);
        
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to transaction");
        }

        return modelMapper.map(transaction, TransactionDTO.class);
    }

    public List<TransactionDTO> getAllTransactions(String userId) {
        log.info("Fetching all transactions for user: {}", userId);
        
        return transactionRepository.findByUserIdAndTransactionDateBetweenAndIsDeletedFalse(
                userId, LocalDateTime.MIN, LocalDateTime.now())
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByType(String userId, String type) {
        log.info("Fetching {} transactions for user: {}", type, userId);
        
        return transactionRepository.findByUserIdAndTypeAndIsDeletedFalse(userId, type)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByCategory(String userId, String category) {
        log.info("Fetching transactions by category: {} for user: {}", category, userId);
        
        return transactionRepository.findByUserIdAndCategoryAndIsDeletedFalse(userId, category)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDivision(String userId, String division) {
        log.info("Fetching transactions by division: {} for user: {}", division, userId);
        
        return transactionRepository.findByUserIdAndDivisionAndIsDeletedFalse(userId, division)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching transactions between {} and {} for user: {}", startDate, endDate, userId);
        
        return transactionRepository.findTransactionsByDateRange(userId, startDate, endDate)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public TransactionDTO updateTransaction(String userId, String transactionId, TransactionDTO transactionDTO) {
        log.info("Updating transaction: {} for user: {}", transactionId, userId);
        
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to transaction");
        }

        // Check if 12 hours have passed since creation
        LocalDateTime createdTime = transaction.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        long hoursPassed = java.time.temporal.ChronoUnit.HOURS.between(createdTime, now);

        if (hoursPassed >= editLimitHours) {
            throw new RuntimeException("Transaction cannot be edited after 12 hours of creation");
        }

        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setDivision(transactionDTO.getDivision());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully: {}", transactionId);
        
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    public void deleteTransaction(String userId, String transactionId) {
        log.info("Deleting transaction: {} for user: {}", transactionId, userId);
        
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to transaction");
        }

        // Soft delete
        transaction.setDeleted(true);
        transaction.setDeletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        log.info("Transaction deleted successfully: {}", transactionId);
    }

    public Double getTotalIncome(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> incomeTransactions = transactionRepository
                .findTransactionsByTypeAndDateRange(userId, "INCOME", startDate, endDate);
        return incomeTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Double getTotalExpense(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> expenseTransactions = transactionRepository
                .findTransactionsByTypeAndDateRange(userId, "EXPENSE", startDate, endDate);
        return expenseTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
