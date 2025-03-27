package takeoff.logisticsservice.msa.delivery.DeliverySequence.presentation.internal;


import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import takeoff.logisticsservice.msa.delivery.DeliverySequence.application.DeliverySequenceService;
import takeoff.logisticsservice.msa.delivery.DeliverySequence.presentation.dto.response.GetCompanyDeliverySequenceResponse;
import takeoff.logisticsservice.msa.delivery.DeliverySequence.presentation.dto.response.GetHubDeliverySequenceResponse;

@RestController
@RequestMapping("/api/v1/app/deliverySequences")
@RequiredArgsConstructor
public class DeliverySequenceInternalController {

  private final DeliverySequenceService deliverySequenceService;

  @GetMapping("/nextCompanyDeliverySequence")
  public GetCompanyDeliverySequenceResponse findNextCompanyDeliverySequence(
      @RequestParam("hubId")UUID hubId
  ) {
    return GetCompanyDeliverySequenceResponse.from(
        deliverySequenceService.findNextCompanyDeliverySequence(hubId)
    );
  }

  @GetMapping("/nextHubDeliverySequence")
  public GetHubDeliverySequenceResponse findNextHubDeliverySequence() {
    return GetHubDeliverySequenceResponse.from(
        deliverySequenceService.findNextHubDeliverySequence()
    );
  }
}
