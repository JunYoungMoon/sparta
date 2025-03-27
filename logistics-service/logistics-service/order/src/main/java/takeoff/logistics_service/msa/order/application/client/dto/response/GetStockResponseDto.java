package takeoff.logistics_service.msa.order.application.client.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetStockResponseDto(
    StockIdResponseDto stockId,
    Integer quantity,
    LocalDateTime updatedAt) {

  public record StockIdResponseDto(UUID productId, UUID hubId) {
  }
}
