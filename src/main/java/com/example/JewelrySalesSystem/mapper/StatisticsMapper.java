package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.entity.Statistics;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {
    Statistics toStatistics(StatisticsCreationRequest request);

    StatisticsResponse toStatisticsResponse(Statistics statistics);

    void updateStatistics(@MappingTarget Statistics statistics, StatisticsUpdateRequest request);
}
