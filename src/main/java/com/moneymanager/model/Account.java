package com.moneymanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String accountName;

    private String accountType; // SAVINGS, CHECKING, INVESTMENT, etc.

    private Double balance;

    private String currency; // USD, EUR, etc.

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isActive;

    private String bankName;

    private String accountNumber; // Encrypted in real scenario

    private Double initialBalance;
}
