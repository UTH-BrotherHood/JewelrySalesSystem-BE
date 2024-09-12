package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.entity.Promotion;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.PromotionMapper;
import com.example.JewelrySalesSystem.repository.PromotionRepository;
import com.example.JewelrySalesSystem.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PromotionServiceTest {

  @Mock
  private PromotionRepository promotionRepository;

  @Mock
  private PromotionMapper promotionMapper;

  @InjectMocks
  private PromotionService promotionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createPromotion_Success() {
    // Arrange
    PromotionCreationRequest request = new PromotionCreationRequest(
            "New Promotion", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(10)
    );

    Promotion promotion = new Promotion();
    promotion.setPromotionId(1);

    PromotionResponse response = new PromotionResponse(
            1, "New Promotion", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(10), LocalDateTime.now(), LocalDateTime.now()
    );

    // Mock behavior
    when(promotionMapper.toPromotion(request)).thenReturn(promotion);
    when(promotionRepository.save(promotion)).thenReturn(promotion);
    when(promotionMapper.toPromotionResponse(promotion)).thenReturn(response);

    // Act
    PromotionResponse result = promotionService.createPromotion(request);

    // Assert
    assertNotNull(result);
    assertEquals("New Promotion", result.getPromotionDescription());
    verify(promotionMapper, times(1)).toPromotion(request);
    verify(promotionRepository, times(1)).save(promotion);
    verify(promotionMapper, times(1)).toPromotionResponse(promotion);
  }

  @Test
  void updatePromotion_Success() {
    // Arrange
    PromotionUpdateRequest request = new PromotionUpdateRequest(
            "Updated Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(15)
    );

    Promotion existingPromotion = new Promotion();
    existingPromotion.setPromotionId(1);

    Promotion updatedPromotion = new Promotion();
    updatedPromotion.setPromotionId(1);

    PromotionResponse response = new PromotionResponse(
            1, "Updated Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(15), LocalDateTime.now(), LocalDateTime.now()
    );

    when(promotionRepository.findById("1")).thenReturn(Optional.of(existingPromotion));
    doNothing().when(promotionMapper).updatePromotion(existingPromotion, request);
    when(promotionRepository.save(existingPromotion)).thenReturn(updatedPromotion);
    when(promotionMapper.toPromotionResponse(updatedPromotion)).thenReturn(response);

    // Act
    PromotionResponse result = promotionService.updatePromotion("1", request);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Description", result.getPromotionDescription());
    verify(promotionMapper, times(1)).updatePromotion(existingPromotion, request);
    verify(promotionRepository, times(1)).save(existingPromotion);
    verify(promotionMapper, times(1)).toPromotionResponse(updatedPromotion);
  }

  @Test
  void updatePromotion_PromotionNotFound() {
    PromotionUpdateRequest request = new PromotionUpdateRequest(
            "Updated Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(15)
    );

    when(promotionRepository.findById("1")).thenReturn(Optional.empty());

    AppException thrown = assertThrows(AppException.class, () -> promotionService.updatePromotion("1", request));
    assertEquals(ErrorCode.PROMOTION_NOT_FOUND, thrown.getErrorCode());
  }

  @Test
  void deletePromotion_Success() {
    when(promotionRepository.existsById("1")).thenReturn(true);

    promotionService.deletePromotion("1");

    verify(promotionRepository, times(1)).deleteById("1");
  }

  @Test
  void deletePromotion_PromotionNotFound() {
    when(promotionRepository.existsById("1")).thenReturn(false);

    AppException thrown = assertThrows(AppException.class, () -> promotionService.deletePromotion("1"));
    assertEquals(ErrorCode.PROMOTION_NOT_FOUND, thrown.getErrorCode());
  }

  @Test
  void getPromotion_Success() {
    Promotion promotion = new Promotion();
    promotion.setPromotionId(1);

    PromotionResponse response = new PromotionResponse(
            1, "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(10), LocalDateTime.now(), LocalDateTime.now()
    );

    when(promotionRepository.findById("1")).thenReturn(Optional.of(promotion));
    when(promotionMapper.toPromotionResponse(promotion)).thenReturn(response);

    PromotionResponse result = promotionService.getPromotion("1");

    assertNotNull(result);
    assertEquals("Description", result.getPromotionDescription());
    verify(promotionRepository, times(1)).findById("1");
    verify(promotionMapper, times(1)).toPromotionResponse(promotion);
  }

  @Test
  void getPromotion_PromotionNotFound() {
    when(promotionRepository.findById("1")).thenReturn(Optional.empty());

    AppException thrown = assertThrows(AppException.class, () -> promotionService.getPromotion("1"));
    assertEquals(ErrorCode.PROMOTION_NOT_FOUND, thrown.getErrorCode());
  }

  @Test
  void getPromotions_Success() {
    Promotion promotion = new Promotion();
    promotion.setPromotionId(1);

    PromotionResponse response = new PromotionResponse(
            1, "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(10), LocalDateTime.now(), LocalDateTime.now()
    );

    Page<Promotion> promotions = new PageImpl<>(Collections.singletonList(promotion));
    Page<PromotionResponse> responses = new PageImpl<>(Collections.singletonList(response));

    when(promotionRepository.findAll(any(Pageable.class))).thenReturn(promotions);
    when(promotionMapper.toPromotionResponse(promotion)).thenReturn(response);

    Page<PromotionResponse> result = promotionService.getPromotions(Pageable.unpaged());

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals("Description", result.getContent().get(0).getPromotionDescription());
    verify(promotionRepository, times(1)).findAll(any(Pageable.class));
    verify(promotionMapper, times(1)).toPromotionResponse(promotion);
  }
}
