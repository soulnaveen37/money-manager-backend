package com.moneymanager.repository;

import com.moneymanager.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByUserId(String userId);

    List<Account> findByUserIdAndIsActiveTrue(String userId);

    Optional<Account> findByIdAndUserId(String id, String userId);

    List<Account> findByUserIdAndAccountType(String userId, String accountType);

    boolean existsByUserIdAndAccountNumber(String userId, String accountNumber);
}
