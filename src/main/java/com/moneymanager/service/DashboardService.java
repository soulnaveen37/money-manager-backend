package com.moneymanager.service;

import com.moneymanager.dto.TransactionDTO;
import com.moneymanager.model.Transaction;
import com.moneymanager.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public Map<String, Object> getMonthlyReport(String userId, int month, int year) {
        log.info("Generating monthly report for user: {} for {}-{}", userId, month, year);
        
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, 28, 23, 59, 59); // Simplified

        List<Transaction> transactions = transactionRepository
                .findTransactionsByDateRange(userId, startDate, endDate);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("month", month);
        report.put("year", year);
        report.put("totalIncome", totalIncome);
        report.put("totalExpense", totalExpense);
        report.put("netBalance", totalIncome - totalExpense);
        report.put("transactionCount", transactions.size());
        report.put("data", transactions);

        return report;
    }

    public Map<String, Object> getWeeklyReport(String userId, int week, int year) {
        log.info("Generating weekly report for user: {} for week {} of {}", userId, week, year);
        
        // Simplified week calculation
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0, 0).plusWeeks(week - 1);
        LocalDateTime endDate = startDate.plusDays(7);

        List<Transaction> transactions = transactionRepository
                .findTransactionsByDateRange(userId, startDate, endDate);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("week", week);
        report.put("year", year);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalIncome", totalIncome);
        report.put("totalExpense", totalExpense);
        report.put("netBalance", totalIncome - totalExpense);
        report.put("transactionCount", transactions.size());
        report.put("data", transactions);

        return report;
    }

    public Map<String, Object> getYearlyReport(String userId, int year) {
        log.info("Generating yearly report for user: {} for year {}", userId, year);
        
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<Transaction> transactions = transactionRepository
                .findTransactionsByDateRange(userId, startDate, endDate);

        double totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("year", year);
        report.put("totalIncome", totalIncome);
        report.put("totalExpense", totalExpense);
        report.put("netBalance", totalIncome - totalExpense);
        report.put("transactionCount", transactions.size());
        report.put("data", transactions);

        return report;
    }

    public Map<String, Object> getCategoryReport(String userId) {
        log.info("Generating category report for user: {}", userId);
        
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        Map<String, Double> categoryExpenses = new HashMap<>();
        Map<String, Double> categoryIncomes = new HashMap<>();

        for (Transaction transaction : transactions) {
            if ("EXPENSE".equals(transaction.getType())) {
                categoryExpenses.merge(transaction.getCategory(), 
                        transaction.getAmount(), Double::sum);
            } else if ("INCOME".equals(transaction.getType())) {
                categoryIncomes.merge(transaction.getCategory(), 
                        transaction.getAmount(), Double::sum);
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("categoryExpenses", categoryExpenses);
        report.put("categoryIncomes", categoryIncomes);
        report.put("totalExpense", categoryExpenses.values().stream().mapToDouble(Double::doubleValue).sum());
        report.put("totalIncome", categoryIncomes.values().stream().mapToDouble(Double::doubleValue).sum());

        return report;
    }
}
