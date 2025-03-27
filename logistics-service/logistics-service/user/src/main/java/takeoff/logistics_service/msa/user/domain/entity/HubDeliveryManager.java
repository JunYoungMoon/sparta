package takeoff.logistics_service.msa.user.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import takeoff.logistics_service.msa.user.domain.vo.DeliveryManagerType;
import takeoff.logistics_service.msa.user.domain.vo.DeliverySequence;
import takeoff.logistics_service.msa.user.domain.vo.HubId;

@Entity
@DiscriminatorValue("HUB_DELIVERY_MANAGER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_delivery_manager")
public class HubDeliveryManager extends DeliveryManager {

    @Embedded
    private HubId hubId;

    protected HubDeliveryManager(String username, String slackEmail, String password, UserRole role, DeliverySequence deliverySequence, UUID identify) {
        super(username, slackEmail, password, role, deliverySequence, DeliveryManagerType.HUB_DELIVERY_MANAGER);
        this.hubId = HubId.from(identify);  // identifier를 hubId로 설정
    }

    @Override
    public String getIdentifier() {
        return this.hubId != null ? this.hubId.getHubIdentifier().toString() : null;
    }

    @Override
    public void updateIdentifier(String identifier) {
        this.hubId = HubId.from(UUID.fromString(identifier));
    }

    public static HubDeliveryManager create(String username, String slackEmail, String password, UserRole role, DeliverySequence deliverySequence, UUID identifier) {
        return new HubDeliveryManager(username, slackEmail, password, role, deliverySequence, identifier);
    }
}
