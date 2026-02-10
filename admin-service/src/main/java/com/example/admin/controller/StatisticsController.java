package com.example.admin.controller;

import com.example.admin.dto.StatsDto;
import com.example.admin.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/global")
    public ResponseEntity<Map<String, Long>> getGlobalCounts() {
        return ResponseEntity.ok(statisticsService.getGlobalCounts());
    }

    @GetMapping("/list")
    public ResponseEntity<List<StatsDto>> getStatsList(
            @RequestParam String type,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(statisticsService.getStatsList(type, start, end));
    }
}