package takeoff.logisticsservice.msa.delivery.delivery.application.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import takeoff.logistics_service.msa.common.config.FeignClientConfig;
import takeoff.logisticsservice.msa.delivery.delivery.application.client.dto.response.GetCompanyDeliverySequenceResponseDto;

@Component
@FeignClient(name = "delivery", contextId = "delivery-deliveryRoute-context",
 configuration = FeignClientConfig.class)
public interface DeliverySequenceClientInternalDelivery {

  @GetMapping("/api/v1/app/deliverySequences/nextCompanyDeliverySequence")
  GetCompanyDeliverySequenceResponseDto findNextCompanyDeliverySequence(
      @RequestParam("hubId") UUID hubId
  );
}
