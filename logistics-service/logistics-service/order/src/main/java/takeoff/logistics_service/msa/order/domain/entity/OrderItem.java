package takeoff.logistics_service.msa.order.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @EmbeddedId
  private OrderItemId id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  private UUID productId;

  private Integer quantity;

  public void enrollOrder(Order order) {
    this.order = order;
  }

  public void modifyQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Builder
  public OrderItem(UUID id, Order order, UUID productId, Integer quantity) {
    this.id = OrderItemId.from(id);
    this.order = order;
    this.productId = productId;
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItem orderItem)) {
      return false;
    }
    return Objects.equals(id, orderItem.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}

