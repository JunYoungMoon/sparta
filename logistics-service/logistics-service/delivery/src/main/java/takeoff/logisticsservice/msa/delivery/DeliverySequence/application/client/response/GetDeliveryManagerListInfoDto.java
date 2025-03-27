package takeoff.logisticsservice.msa.delivery.DeliverySequence.application.client.response;

public record GetDeliveryManagerListInfoDto(
    Long deliveryManagerId,
    String username,
    String slackEmail,
    String deliveryManagerType,
    String identifier,
    int sequenceNumber
) {

}
