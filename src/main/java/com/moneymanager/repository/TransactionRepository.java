package com.moneymanager.repository;

import com.moneymanager.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByUserId(String userId);

    List<Transaction> findByUserIdAndTypeAndIsDeletedFalse(String userId, String type);

    List<Transaction> findByUserIdAndCategoryAndIsDeletedFalse(String userId, String category);

    List<Transaction> findByUserIdAndDivisionAndIsDeletedFalse(String userId, String division);

    List<Transaction> findByUserIdAndTransactionDateBetweenAndIsDeletedFalse(
            String userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserIdAndCategoryAndDivisionAndIsDeletedFalse(
            String userId, String category, String division);

    @Query("{'userId': ?0, 'transactionDate': {'$gte': ?1, '$lte': ?2}, 'isDeleted': false}")
    List<Transaction> findTransactionsByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'userId': ?0, 'type': ?1, 'transactionDate': {'$gte': ?2, '$lte': ?3}, 'isDeleted': false}")
    List<Transaction> findTransactionsByTypeAndDateRange(String userId, String type, LocalDateTime startDate, LocalDateTime endDate);

    int countByUserIdAndTypeAndIsDeletedFalse(String userId, String type);
}
