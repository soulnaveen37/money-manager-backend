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
public class TransferDTO {

    private String id;

    @NotBlank(message = "From account ID is required")
    private String fromAccountId;

    @NotBlank(message = "To account ID is required")
    private String toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Transfer date is required")
    private LocalDateTime transferDate;

    private LocalDateTime createdAt;

    @Builder.Default
    private String status = "PENDING";

    private String reference;
}
