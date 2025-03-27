package takeoff.logistics_service.msa.order.application.service;

import static takeoff.logistics_service.msa.common.domain.UserRole.COMPANY_MANAGER;
import static takeoff.logistics_service.msa.common.domain.UserRole.HUB_MANAGER;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import takeoff.logistics_service.msa.common.domain.UserRole;
import takeoff.logistics_service.msa.common.exception.BusinessException;
import takeoff.logistics_service.msa.common.exception.code.CommonErrorCode;
import takeoff.logistics_service.msa.order.application.client.CompanyClient;
import takeoff.logistics_service.msa.order.application.client.DeliveryClient;
import takeoff.logistics_service.msa.order.application.client.StockClient;
import takeoff.logistics_service.msa.order.application.client.UserClient;
import takeoff.logistics_service.msa.order.application.client.dto.request.AbortStockRequestDto;
import takeoff.logistics_service.msa.order.application.client.dto.request.PostDeliveryRequestDto;
import takeoff.logistics_service.msa.order.application.client.dto.request.PostDeliveryRoutesRequestDto;
import takeoff.logistics_service.msa.order.application.client.dto.request.PrePareStockRequestDto;
import takeoff.logistics_service.msa.order.application.client.dto.request.StockIdRequestDto;
import takeoff.logistics_service.msa.order.application.client.dto.request.StockItemRequestDto;
import takeoff.logistics_service.msa.order.application.dto.PaginatedResultDto;
import takeoff.logistics_service.msa.order.application.dto.request.SearchOrderRequestDto;
import takeoff.logistics_service.msa.order.application.dto.response.SearchOrderResponseDto;
import takeoff.logistics_service.msa.order.application.exception.OrderBusinessException;
import takeoff.logistics_service.msa.order.application.exception.OrderErrorCode;
import takeoff.logistics_service.msa.order.domain.entity.ModifyQuantityCommand;
import takeoff.logistics_service.msa.order.domain.entity.Order;
import takeoff.logistics_service.msa.order.domain.entity.OrderId;
import takeoff.logistics_service.msa.order.domain.entity.OrderItem;
import takeoff.logistics_service.msa.order.domain.repository.OrderRepository;
import takeoff.logistics_service.msa.order.presentation.dto.request.PatchOrderRequest;
import takeoff.logistics_service.msa.order.presentation.dto.request.PostOrderRequest;
import takeoff.logistics_service.msa.order.presentation.dto.response.PatchOrderResponse;
import takeoff.logistics_service.msa.order.presentation.dto.response.PostOrderResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final DeliveryClient deliveryClient;
  private final StockClient stockClient;
  private final CompanyClient companyClient;
  private final UserClient userClient;


  @Transactional
  public PostOrderResponse saveOrder(PostOrderRequest dto) {
    Order order = Order.builder().id(UUID.randomUUID()).supplierId(dto.supplierId())
        .orderItems(dto.orderItems().stream()
            .map(orderItemDto -> OrderItem.builder().id(UUID.randomUUID())
                .productId(orderItemDto.productId())
                .quantity(orderItemDto.quantity()).build()).toList()).customerId(dto.customerId())
        .address(dto.deliveryAddress()).requestNotes(dto.requestNotes()).build();

    // 배송 경로 추적
    // TODO : 호출하는 API 가 출발지를 정하는 것은 아니라서 출발 허브 id 를 바꾸는 로직을 리펙터링해도 됨
    UUID fromHubId = stockClient.getStock(dto.orderItems().get(0).productId()).stockId()
        .hubId(); // 상품 소재지 허브
    UUID toHubId = companyClient.findByCompanyId(dto.companyId()).hubId(); // 고객회사 주소지 허브
    order.registerHub(toHubId);

    UUID deliveryId = deliveryClient.saveDelivery(new PostDeliveryRequestDto(
        order.getId().getOrderId(),
        dto.customerId(),
        fromHubId,
        toHubId
    ));
    log.info(String.valueOf(fromHubId));
    log.info(String.valueOf(toHubId));

    order.modifyDeliveryId(deliveryId);
    // TODO : 비동기로직으로 수정

    deliveryClient.saveDeliveryRoute(
        new PostDeliveryRoutesRequestDto(deliveryId, fromHubId, toHubId));

    // 재고 관리
    List<StockItemRequestDto> stocks = dto.orderItems().stream().map(
            item -> new StockItemRequestDto(
                new StockIdRequestDto(item.productId(), parseHubId(item.productId())), item.quantity()))
        .toList();

    PrePareStockRequestDto prePareStockRequestDto = new PrePareStockRequestDto(stocks);
    stockClient.prepareStock(prePareStockRequestDto);

    orderRepository.save(order);
    return PostOrderResponse.from(order);

  }

  @Transactional
  public PatchOrderResponse updateOrder(PatchOrderRequest dto, UUID orderId) {
    Order order = orderRepository.findById(OrderId.from(orderId))
        .orElseThrow(() -> OrderBusinessException.from(OrderErrorCode.ORDER_NOT_FOUND));

    List<ModifyQuantityCommand> commands = dto.orderItems().stream().map(
        orderItemDto -> ModifyQuantityCommand.from(orderItemDto.productId(),
            orderItemDto.quantity())).toList();

    order.modifyAllQuantityByProduct(commands);

    return PatchOrderResponse.from(order);
  }

  @Transactional
  public void deleteOrder(UUID orderId, Long userId) {
    Order order = orderRepository.findById(OrderId.from(orderId))
        .orElseThrow(() -> OrderBusinessException.from(OrderErrorCode.ORDER_NOT_FOUND));

    // 재고 관리
    List<StockItemRequestDto> stocks = order.getOrderItems().stream().map(
        item -> new StockItemRequestDto(
            new StockIdRequestDto(item.getProductId(), parseHubId(item.getProductId())),
            item.getQuantity())).toList();

    AbortStockRequestDto abortStockRequestDto = new AbortStockRequestDto(stocks);
    stockClient.abortStock(abortStockRequestDto);

    // 주문 같이 삭제
    deliveryClient.deleteDelivery(order.getDeliveryId());
    deliveryClient.deleteDeliveryRoutes(order.getDeliveryId());
    order.delete(userId);
  }

  @Transactional
  public PaginatedResultDto<SearchOrderResponseDto> searchOrder(SearchOrderRequestDto dto,
      Long userId, UserRole userRole) {

    return switch (userRole) {
      case MASTER_ADMIN ->
          PaginatedResultDto.from(orderRepository.findAllBySearchParams(dto.toSearchCriteria()));
      case HUB_MANAGER -> {
        validateHubManagerAccess(dto.hubId(), userId, userRole);
        yield PaginatedResultDto.from(
            orderRepository.findAllBySearchParams(dto.toSearchCriteria()));
      }
      case COMPANY_MANAGER -> {
        validateCompanyManagerAccess(dto.supplierId(), userId, userRole);
        yield PaginatedResultDto.from(
            orderRepository.findAllBySearchParams(dto.toSearchCriteria(userId)));
      }
      case HUB_DELIVERY_MANAGER -> {
        List<UUID> deliveryIds = deliveryClient.findAllDeliveryRoutes_DeliveryIdByDeliveryManagerId(
            userId);
        yield PaginatedResultDto.from(
            orderRepository.findAllBySearchParams(dto.toSearchCriteria(deliveryIds)));
      }
      case COMPANY_DELIVERY_MANAGER -> {
        List<UUID> deliveryIds = deliveryClient.findAllDeliveryIdByUser(userId);
        yield PaginatedResultDto.from(
            orderRepository.findAllBySearchParams(dto.toSearchCriteria(deliveryIds)));
      }
    };
  }

  private void validateHubManagerAccess(UUID resourceId, Long userId, UserRole userRole) {
    if (userRole.equals(HUB_MANAGER) && !getHubId(userId).equals(resourceId)) {
      throw BusinessException.from(CommonErrorCode.FORBIDDEN);
    }
  }

  private void validateCompanyManagerAccess(UUID resourceId, Long userId, UserRole userRole) {
    if (userRole.equals(COMPANY_MANAGER) && !getCompanyId(userId).equals(resourceId)) {
      throw BusinessException.from(CommonErrorCode.FORBIDDEN);
    }
  }


  private UUID parseHubId(UUID pid) {
    return stockClient.getStock(pid).stockId().hubId();
  }

  private UUID getHubId(Long userId) {
    return userClient.findByUserId(userId).hubId();
  }

  private UUID getCompanyId(Long userId) {
    return userClient.findByUserId(userId).companyId();
  }
}
