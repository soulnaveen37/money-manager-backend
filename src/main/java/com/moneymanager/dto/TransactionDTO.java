package com.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private String id;

    @NotBlank(message = "Type is required (INCOME or EXPENSE)")
    private String type;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Division is required (Office or Personal)")
    private String division;

    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String accountId;

    private String notes;

    @Builder.Default
    private String status = "COMPLETED";
}
