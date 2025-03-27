package takeoff.logisticsservice.msa.delivery.DeliverySequence.presentation.dto.request;

import java.util.UUID;

public record GetCompanyDeliverySequenceRequest(
    UUID hubId
) {

}
