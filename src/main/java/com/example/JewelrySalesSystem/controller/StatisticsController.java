package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping
    public ResponseEntity<StatisticsResponse> createStatistics(@RequestBody StatisticsCreationRequest request) {
        return new ResponseEntity<>(statisticsService.createStatistics(request), HttpStatus.CREATED);
    }

    @PutMapping("/{statisticId}")
    public ResponseEntity<StatisticsResponse> updateStatistics(
            @PathVariable Integer statisticId,
            @RequestBody StatisticsUpdateRequest request) {
        return ResponseEntity.ok(statisticsService.updateStatistics(statisticId, request));
    }

    @DeleteMapping("/{statisticId}")
    public ResponseEntity<Void> deleteStatistics(@PathVariable Integer statisticId) {
        statisticsService.deleteStatistics(statisticId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{statisticId}")
    public ResponseEntity<StatisticsResponse> getStatistics(@PathVariable Integer statisticId) {
        return ResponseEntity.ok(statisticsService.getStatistics(statisticId));
    }

    @GetMapping
    public ResponseEntity<Page<StatisticsResponse>> getStatistics(Pageable pageable) {
        return ResponseEntity.ok(statisticsService.getStatistics(pageable));
    }
}
