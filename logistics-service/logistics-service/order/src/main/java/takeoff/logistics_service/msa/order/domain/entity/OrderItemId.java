package takeoff.logistics_service.msa.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OrderItemId implements Serializable {

  @UuidGenerator
  @Column(name = "order_item_id", nullable = false)
  private UUID orderItemId;

  public OrderItemId(UUID orderItemId) {
    this.orderItemId = orderItemId;
  }

  public static OrderItemId from(UUID orderItemId) {
    return new OrderItemId(orderItemId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItemId that)) {
      return false;
    }
    return Objects.equals(orderItemId, that.orderItemId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderItemId);
  }
}
