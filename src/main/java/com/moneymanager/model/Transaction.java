package com.moneymanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String type; // INCOME or EXPENSE

    private String description;

    private Double amount;

    private String category; // Food, Fuel, Movie, Medical, Loan, Salary, Freelance, etc.

    private String division; // Office or Personal

    private LocalDateTime transactionDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt; // For soft delete

    private boolean isDeleted;

    private String accountId;

    private String notes;

    private String status; // PENDING, COMPLETED, FAILED

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", division='" + division + '\'' +
                ", transactionDate=" + transactionDate +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}
