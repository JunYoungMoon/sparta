package takeoff.logistics_service.msa.order.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import takeoff.logistics_service.msa.common.config.FeignClientConfig;
import takeoff.logistics_service.msa.order.application.client.dto.response.GetUserResponseDto;

@Component
@FeignClient(name = "user", configuration = FeignClientConfig.class)
public interface UserClient {

  @GetMapping("api/v1/app/users/{userId}")
  GetUserResponseDto findByUserId(@PathVariable("userId") Long userId);

}
