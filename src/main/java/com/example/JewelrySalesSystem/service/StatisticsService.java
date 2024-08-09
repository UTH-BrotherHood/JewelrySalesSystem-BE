package com.example.JewelrySalesSystem.service;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.entity.Statistics;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.StatisticsMapper;
import com.example.JewelrySalesSystem.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final StatisticsMapper statisticsMapper;

    public StatisticsResponse createStatistics(StatisticsCreationRequest request) {
        Statistics statistics = statisticsMapper.toStatistics(request);
        Statistics savedStatistics = statisticsRepository.save(statistics);
        return statisticsMapper.toStatisticsResponse(savedStatistics);
    }

    public StatisticsResponse updateStatistics(String statisticId, StatisticsUpdateRequest request) {
        Statistics statistics = statisticsRepository.findById(statisticId)
                .orElseThrow(() -> new AppException(ErrorCode.STATISTICS_NOT_FOUND));
        statisticsMapper.updateStatistics(statistics, request);
        Statistics updatedStatistics = statisticsRepository.save(statistics);
        return statisticsMapper.toStatisticsResponse(updatedStatistics);
    }

    public void deleteStatistics(String statisticId) {
        if (!statisticsRepository.existsById(statisticId)) {
            throw new AppException(ErrorCode.STATISTICS_NOT_FOUND);
        }
        statisticsRepository.deleteById(statisticId);
    }

    public StatisticsResponse getStatistics(String statisticId) {
        Statistics statistics = statisticsRepository.findById(statisticId)
                .orElseThrow(() -> new AppException(ErrorCode.STATISTICS_NOT_FOUND));
        return statisticsMapper.toStatisticsResponse(statistics);
    }

    public Page<StatisticsResponse> getStatistics(Pageable pageable) {
        return statisticsRepository.findAll(pageable)
                .map(statisticsMapper::toStatisticsResponse);
    }
}
