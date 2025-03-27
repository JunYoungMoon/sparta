package takeoff.logisticsservice.msa.delivery.DeliverySequence.application.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import takeoff.logistics_service.msa.common.config.FeignClientConfig;
import takeoff.logisticsservice.msa.delivery.DeliverySequence.application.client.response.GetDeliveryManagerListInternalResponseDto;

@Component
@FeignClient(name = "user", contextId = "deliverySequence-managers-context",
    configuration = FeignClientConfig.class)
public interface UserClient {

  @GetMapping("/api/v1/app/users/delivery-managers/company")
  GetDeliveryManagerListInternalResponseDto findAllCompanyDeliveryManagerByHubId(
      @RequestParam UUID hubId
  );

  @GetMapping("/api/v1/app/users/delivery-managers/hub")
  GetDeliveryManagerListInternalResponseDto findAllHubDeliveryManager();

}
