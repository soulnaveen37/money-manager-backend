package com.moneymanager.service;

import com.moneymanager.dto.AccountDTO;
import com.moneymanager.model.Account;
import com.moneymanager.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public AccountDTO createAccount(String userId, AccountDTO accountDTO) {
        log.info("Creating account for user: {}", userId);
        
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setUserId(userId);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setActive(true);
        account.setInitialBalance(accountDTO.getBalance());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount.getId());
        
        return modelMapper.map(savedAccount, AccountDTO.class);
    }

    public AccountDTO getAccountById(String userId, String accountId) {
        log.info("Fetching account: {} for user: {}", accountId, userId);
        
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return modelMapper.map(account, AccountDTO.class);
    }

    public List<AccountDTO> getAllAccounts(String userId) {
        log.info("Fetching all accounts for user: {}", userId);
        
        return accountRepository.findByUserId(userId)
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    public List<AccountDTO> getActiveAccounts(String userId) {
        log.info("Fetching active accounts for user: {}", userId);
        
        return accountRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    public AccountDTO updateAccount(String userId, String accountId, AccountDTO accountDTO) {
        log.info("Updating account: {} for user: {}", accountId, userId);
        
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setAccountName(accountDTO.getAccountName());
        account.setBalance(accountDTO.getBalance());
        account.setBankName(accountDTO.getBankName());
        account.setUpdatedAt(LocalDateTime.now());

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully: {}", accountId);
        
        return modelMapper.map(updatedAccount, AccountDTO.class);
    }

    public void deactivateAccount(String userId, String accountId) {
        log.info("Deactivating account: {} for user: {}", accountId, userId);
        
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setActive(false);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
        
        log.info("Account deactivated successfully: {}", accountId);
    }

    public Double getTotalBalance(String userId) {
        log.info("Calculating total balance for user: {}", userId);
        
        return accountRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
}
