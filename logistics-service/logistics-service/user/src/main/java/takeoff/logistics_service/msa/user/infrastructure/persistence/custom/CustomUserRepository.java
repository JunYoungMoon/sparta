package takeoff.logistics_service.msa.user.infrastructure.persistence.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import takeoff.logistics_service.msa.user.domain.entity.DeliveryManager;

public interface CustomUserRepository {
    Page<DeliveryManager> findAllDeliveryManagers(Specification<DeliveryManager> spec, Pageable pageable);
}
