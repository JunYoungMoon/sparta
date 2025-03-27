package takeoff.logistics_service.msa.user.domain.service;

import takeoff.logistics_service.msa.user.domain.vo.DeliveryManagerType;

public record DeliveryManagerSearchCondition(
        String hubId,
        DeliveryManagerType deliveryManagerType
) {
}
