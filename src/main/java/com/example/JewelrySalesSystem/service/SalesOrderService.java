package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;

import com.example.JewelrySalesSystem.entity.*;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderDetailMapper;
import com.example.JewelrySalesSystem.mapper.SalesOrderMapper;
import com.example.JewelrySalesSystem.mapper.WarrantyMapper;
import com.example.JewelrySalesSystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final WarrantyRepository warrantyRepository;
    private final WarrantyMapper warrantyMapper;
    private final WarrantyService warrantyService;
    private final CartService cartService;
    private final PromotionRepository promotionRepository;
    private final SalesOrderDetailMapper salesOrderDetailMapper;
    private final ReturnPolicyRepository returnPolicyRepository;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public SalesOrderResponse createSalesOrder(SalesOrderCreationRequest request) {
        // Check if customer and employee exist
        if (!customerRepository.existsById(request.getCustomerId())) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        // Validate payment method
        if (!paymentMethodRepository.existsById(request.getPaymentMethodId())) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND);
        }

        // Fetch Cart using employeeId
        Cart cart = cartRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Check if cart has items
        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_IS_EMPTY);
        }

        // Calculate total amount from cart items
        BigDecimal totalAmount = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate member rank discount
        BigDecimal rankDiscount = calculateRankDiscount(request.getCustomerId(), totalAmount);

        // Apply promotion if available
        BigDecimal discountedTotalAmount = totalAmount;
        if (request.getPromotionCode() != null && !request.getPromotionCode().isEmpty()) {
            Promotion promotion = promotionRepository.findByPromotionCode(request.getPromotionCode())
                    .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

            if (LocalDateTime.now().isBefore(promotion.getStartDate()) || LocalDateTime.now().isAfter(promotion.getEndDate())) {
                throw new AppException(ErrorCode.PROMOTION_EXPIRED);
            }

            BigDecimal discount = totalAmount.multiply(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            discountedTotalAmount = discountedTotalAmount.subtract(discount);
        }

        // Create SalesOrder and link to Cart
        SalesOrder salesOrder = salesOrderMapper.toSalesOrder(request);
        salesOrder.setCartId(cart.getCartId());
        salesOrder.setOrderDate(LocalDateTime.now());
        salesOrder.setOriginalTotalAmount(totalAmount); // Store original total amount
        salesOrder.setDiscountedByRank(rankDiscount); // Store member rank discount
        salesOrder.setDiscountedTotalAmount(discountedTotalAmount.subtract(rankDiscount)); // Store discounted total amount

        // Set PaymentMethod
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));
        salesOrder.setPaymentMethod(paymentMethod);

        // Create ReturnPolicy
        ReturnPolicy returnPolicy = createReturnPolicyForOrder(salesOrder.getOrderId());
        salesOrder.setReturnPolicy(returnPolicy);

        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);

        // Update customer reward points
        BigDecimal discountAmount = totalAmount.subtract(discountedTotalAmount);
        BigDecimal rewardPoints = discountAmount.multiply(BigDecimal.valueOf(0.1)); // Example, 10% reward points

        customerService.addRewardPoints(request.getCustomerId(), rewardPoints, "Reward points from order ID: " + savedSalesOrder.getOrderId());
        createSalesOrderDetailsFromCart(savedSalesOrder.getOrderId(), cart, request.getPromotionCode());
        // Clear cart
        cartService.clearCart(request.getEmployeeId()); // Clear cart using employeeId

        // Fetch Customer for email
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        // Send invoice via email
        String invoiceContent = generateInvoiceContent(savedSalesOrder); // Generate invoice content
        emailService.sendInvoiceEmail(customer.getEmail(), "Your Invoice", invoiceContent);

        // Check and notify rank upgrade if any
        if (customerService.checkForRankUpgrade(request.getCustomerId())) {
            String rankUpgradeContent = generateRankUpgradeNotification(customer); // Generate rank upgrade notification
            emailService.sendInvoiceEmail(customer.getEmail(), "Rank Upgrade Notification", rankUpgradeContent);
        }

        return salesOrderMapper.toSalesOrderResponse(savedSalesOrder);
    }


    private String generateInvoiceContent(SalesOrder savedSalesOrder) {
        // Giả sử bạn có một phương thức để lấy chi tiết đơn hàng
        List<SalesOrderDetail> orderDetails = salesOrderDetailRepository.findByOrderId(savedSalesOrder.getOrderId());

        // Tạo nội dung hóa đơn HTML từ thông tin đơn hàng
        StringBuilder content = new StringBuilder();
        content.append("<h1>Hóa Đơn</h1>")
                .append("<p>Mã Đơn Hàng: ").append(savedSalesOrder.getOrderId()).append("</p>")
                .append("<p>Ngày Đặt Hàng: ").append(savedSalesOrder.getOrderDate()).append("</p>")
                .append("<table border='1' cellpadding='5' cellspacing='0'>")
                .append("<thead><tr><th>Sản phẩm</th><th>Số lượng</th><th>Đơn Giá</th><th>Thành Tiền</th></tr></thead>")
                .append("<tbody>");

        for (SalesOrderDetail detail : orderDetails) {
            content.append("<tr>")
                    .append("<td>").append(detail.getProductId()).append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(detail.getUnitPrice()).append("</td>")
                    .append("<td>").append(detail.getTotalPrice()).append("</td>")
                    .append("</tr>");
        }

        content.append("</tbody>")
                .append("</table>")
                .append("<p>Tổng Giá Trị: ").append(savedSalesOrder.getOriginalTotalAmount()).append("</p>")
                .append("<p>Giảm Giá Theo Hạng: ").append(savedSalesOrder.getDiscountedByRank()).append("</p>")
                .append("<p>Giá Trị Sau Giảm Giá: ").append(savedSalesOrder.getDiscountedTotalAmount()).append("</p>")
                .append("<p>Cảm ơn bạn đã mua hàng!</p>");

        return content.toString();
    }
    private String generateRankUpgradeNotification(Customer customer) {
        String rank = customer.getRankLevel();
        String benefits = "";

        // Thêm thông tin lợi ích tùy thuộc vào cấp bậc
        switch (rank) {
            case "Bronze":
                benefits = "5% giảm giá cho các đơn hàng tiếp theo.";
                break;
            case "Silver":
                benefits = "10% giảm giá và quà tặng miễn phí cho các đơn hàng tiếp theo.";
                break;
            case "Gold":
                benefits = "15% giảm giá, quà tặng miễn phí và giao hàng miễn phí.";
                break;
            case "Platinum":
                benefits = "20% giảm giá, quà tặng cao cấp, và dịch vụ chăm sóc khách hàng ưu tiên.";
                break;
            default:
                benefits = "Cảm ơn bạn đã là khách hàng thân thiết của chúng tôi!";
                break;
        }

        // Tạo nội dung thông báo lên hạng
        return "<h1>Chúc Mừng!</h1>" +
                "<p>Bạn đã được nâng cấp lên hạng <strong>" + rank + "</strong>!</p>" +
                "<p>Chúc mừng bạn đã trở thành khách hàng thân thiết của chúng tôi.</p>" +
                "<p>Lợi ích mới của bạn bao gồm: " + benefits + "</p>" +
                "<p>Cảm ơn bạn đã ủng hộ và gắn bó với chúng tôi!</p>";
    }


    private BigDecimal calculateRankDiscount(String customerId, BigDecimal totalAmount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        BigDecimal discount = BigDecimal.ZERO;
        switch (customer.getRankLevel()) {
            case "Gold":
                discount = totalAmount.multiply(BigDecimal.valueOf(0.1)); // 10% discount
                break;
            case "Platinum":
                discount = totalAmount.multiply(BigDecimal.valueOf(0.15)); // 15% discount
                break;
            case "Silver":
                discount = totalAmount.multiply(BigDecimal.valueOf(0.05)); // 5% discount
                break;
            default:
                // Bronze rank has no discount
                break;
        }
        return discount;
    }

    private ReturnPolicy createReturnPolicyForOrder(String orderId) {
        ReturnPolicy returnPolicy = new ReturnPolicy();
        returnPolicy.setDescription("This order can be returned within 30 days of purchase.");
        returnPolicy.setEffectiveDate(LocalDateTime.now());
        returnPolicy.setExpiryDate(LocalDateTime.now().plusDays(30));
        returnPolicyRepository.save(returnPolicy);
        return returnPolicy;
    }


    private void createSalesOrderDetailsFromCart(String orderId, Cart cart, String promotionCode) {
        List<CartItem> cartItems = cart.getItems();

        // Check if all products exist and have sufficient stock
        List<String> invalidProductIds = cartItems.stream()
                .filter(cartItem -> !productRepository.existsById(cartItem.getProductId()))
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<String> insufficientStockProductIds = cartItems.stream()
                .filter(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
                    return product == null || product.getStockQuantity() < cartItem.getQuantity();
                })
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        if (!insufficientStockProductIds.isEmpty()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Create SalesOrderDetails
        List<SalesOrderDetail> salesOrderDetails = cartItems.stream()
                .map(cartItem -> {
                    BigDecimal totalPrice = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                    // Calculate price after discount
                    BigDecimal priceAfterDiscount = totalPrice;
                    if (promotionCode != null && !promotionCode.isEmpty()) {
                        Promotion promotion = promotionRepository.findByPromotionCode(promotionCode)
                                .orElse(null);
                        if (promotion != null) {
                            BigDecimal discount = totalPrice.multiply(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
                            priceAfterDiscount = totalPrice.subtract(discount);
                        }
                    }

                    return SalesOrderDetail.builder()
                            .orderId(orderId)
                            .productId(cartItem.getProductId())
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getPrice())
                            .totalPrice(totalPrice)
                            .priceAfterDiscount(priceAfterDiscount) // Set price after discount
                            .build();
                }).collect(Collectors.toList());

        salesOrderDetailRepository.saveAll(salesOrderDetails);

        // Update product stock and create warranties
        cartItems.forEach(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                productRepository.save(product);

                // Create warranty
                WarrantyCreationRequest warrantyRequest = WarrantyCreationRequest.builder()
                        .productId(cartItem.getProductId())
                        .warrantyStartDate(LocalDateTime.now())
                        .warrantyEndDate(LocalDateTime.now().plusYears(1))
                        .build();
                warrantyService.createWarranty(warrantyRequest);
            }
        });
    }


    public void deleteSalesOrder(String orderId) {
        if (!salesOrderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.SALES_ORDER_NOT_FOUND);
        }
        salesOrderRepository.deleteById(orderId);
    }

    public SalesOrderResponse getSalesOrder(String orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOT_FOUND));
        return salesOrderMapper.toSalesOrderResponse(salesOrder);
    }

    public Page<SalesOrderResponse> getSalesOrders(Pageable pageable) {
        return salesOrderRepository.findAll(pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public Page<SalesOrderResponse> getSalesOrdersByEmployeeId(String employeeId, Pageable pageable) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        return salesOrderRepository.findByEmployeeId(employeeId, pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public Page<SalesOrderResponse> getSalesOrdersByCustomerId(String customerId, Pageable pageable) {
        if (!customerRepository.existsById(customerId)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        return salesOrderRepository.findByCustomerId(customerId, pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public SalesOrderWithDetailsResponse getSalesOrderWithDetails(String orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOT_FOUND));

        List<SalesOrderDetail> orderDetails = salesOrderDetailRepository.findByOrderId(orderId);

        // Convert SalesOrderDetail list to SalesOrderDetailResponse list
        List<SalesOrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(salesOrderDetailMapper::toSalesOrderDetailResponse)
                .collect(Collectors.toList());

        return SalesOrderWithDetailsResponse.builder()
                .salesOrder(salesOrderMapper.toSalesOrderResponse(salesOrder))
                .orderDetails(orderDetailResponses)
                .build();
    }
}
