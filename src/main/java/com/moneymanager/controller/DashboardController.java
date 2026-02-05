package com.moneymanager.controller;

import com.moneymanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /dashboard/monthly - Getting monthly report for user: {}", userId);
        
        Map<String, Object> report = dashboardService.getMonthlyReport(userId, month, year);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Monthly report retrieved successfully");
        response.put("data", report);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklyReport(
            @RequestParam int week,
            @RequestParam int year,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /dashboard/weekly - Getting weekly report for user: {}", userId);
        
        Map<String, Object> report = dashboardService.getWeeklyReport(userId, week, year);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Weekly report retrieved successfully");
        response.put("data", report);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly")
    public ResponseEntity<Map<String, Object>> getYearlyReport(
            @RequestParam int year,
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /dashboard/yearly - Getting yearly report for user: {}", userId);
        
        Map<String, Object> report = dashboardService.getYearlyReport(userId, year);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Yearly report retrieved successfully");
        response.put("data", report);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategoryReport(
            @RequestHeader("X-User-Id") String userId) {
        log.info("GET /dashboard/categories - Getting category report for user: {}", userId);
        
        Map<String, Object> report = dashboardService.getCategoryReport(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category report retrieved successfully");
        response.put("data", report);
        
        return ResponseEntity.ok(response);
    }
}
