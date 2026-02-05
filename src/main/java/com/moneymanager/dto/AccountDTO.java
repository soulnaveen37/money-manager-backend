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
public class AccountDTO {

    private String id;

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance;

    @Builder.Default
    private String currency = "USD";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    private boolean isActive = true;

    private String bankName;

    private String accountNumber;

    private Double initialBalance;
}
