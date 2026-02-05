package com.moneymanager.service;

import com.moneymanager.dto.TransferDTO;
import com.moneymanager.model.Account;
import com.moneymanager.model.Transfer;
import com.moneymanager.repository.AccountRepository;
import com.moneymanager.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public TransferDTO createTransfer(String userId, TransferDTO transferDTO) {
        log.info("Creating transfer for user: {} from account: {} to account: {}", 
                userId, transferDTO.getFromAccountId(), transferDTO.getToAccountId());

        // Validate accounts exist and belong to user
        Account fromAccount = accountRepository.findByIdAndUserId(transferDTO.getFromAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        
        Account toAccount = accountRepository.findByIdAndUserId(transferDTO.getToAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // Check if sufficient balance
        if (fromAccount.getBalance() < transferDTO.getAmount()) {
            throw new RuntimeException("Insufficient balance in from account");
        }

        // Create transfer record
        Transfer transfer = modelMapper.map(transferDTO, Transfer.class);
        transfer.setUserId(userId);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setStatus("COMPLETED");
        transfer.setReference(UUID.randomUUID().toString());

        // Update account balances
        fromAccount.setBalance(fromAccount.getBalance() - transferDTO.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transferDTO.getAmount());
        fromAccount.setUpdatedAt(LocalDateTime.now());
        toAccount.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transfer savedTransfer = transferRepository.save(transfer);
        log.info("Transfer completed successfully: {}", savedTransfer.getId());

        return modelMapper.map(savedTransfer, TransferDTO.class);
    }

    public List<TransferDTO> getTransfersByUser(String userId) {
        log.info("Fetching transfers for user: {}", userId);
        
        return transferRepository.findByUserId(userId)
                .stream()
                .map(transfer -> modelMapper.map(transfer, TransferDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransferDTO> getTransfersByAccount(String accountId) {
        log.info("Fetching transfers for account: {}", accountId);
        
        List<Transfer> fromTransfers = transferRepository.findByFromAccountId(accountId);
        List<Transfer> toTransfers = transferRepository.findByToAccountId(accountId);
        
        List<Transfer> allTransfers = new ArrayList<>(fromTransfers);
        allTransfers.addAll(toTransfers);
        
        return allTransfers.stream()
                .distinct()
                .map(transfer -> modelMapper.map(transfer, TransferDTO.class))
                .collect(Collectors.toList());
    }

    public List<TransferDTO> getTransfersByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching transfers for user: {} between {} and {}", userId, startDate, endDate);
        
        return transferRepository.findByUserIdAndTransferDateBetween(userId, startDate, endDate)
                .stream()
                .map(transfer -> modelMapper.map(transfer, TransferDTO.class))
                .collect(Collectors.toList());
    }
}
