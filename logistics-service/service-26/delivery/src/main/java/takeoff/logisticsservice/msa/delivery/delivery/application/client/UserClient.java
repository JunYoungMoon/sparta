package takeoff.logisticsservice.msa.delivery.delivery.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import takeoff.logistics_service.msa.common.config.FeignClientConfig;
import takeoff.logisticsservice.msa.delivery.delivery.application.client.dto.response.GetUserResponseDto;

@Component
@FeignClient(name = "user", contextId = "delivery-deliveryManagers-context",
    configuration = FeignClientConfig.class)
public interface UserClient {

  @GetMapping
  GetUserResponseDto findByUserId(@RequestParam("userId") Long userId);

}
