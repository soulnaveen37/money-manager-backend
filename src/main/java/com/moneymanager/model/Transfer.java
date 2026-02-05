package com.moneymanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String fromAccountId;

    private String toAccountId;

    private Double amount;

    private String description;

    private LocalDateTime transferDate;

    private LocalDateTime createdAt;

    private String status; // PENDING, COMPLETED, FAILED

    private String reference;
}
