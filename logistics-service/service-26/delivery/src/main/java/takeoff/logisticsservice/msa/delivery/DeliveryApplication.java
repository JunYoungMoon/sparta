package takeoff.logisticsservice.msa.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan({"takeoff.logistics_service.msa.common", "takeoff.logisticsservice.msa.delivery"})

public class DeliveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeliveryApplication.class, args);
  }

}
