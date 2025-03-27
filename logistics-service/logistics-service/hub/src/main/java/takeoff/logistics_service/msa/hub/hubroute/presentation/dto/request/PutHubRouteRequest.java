package takeoff.logistics_service.msa.hub.hubroute.presentation.dto.request;

import java.util.UUID;
import takeoff.logistics_service.msa.hub.hubroute.application.dto.request.PutHubRouteRequestDto;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Distance;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Duration;

/**
 * @author : hanjihoon
 * @Date : 2025. 03. 15.
 */
public record PutHubRouteRequest(UUID fromHubId,
                                 UUID toHubId,
                                 Distance distance,
                                 Duration duration) {

    public PutHubRouteRequestDto toApplicationDto() {
        return PutHubRouteRequestDto.builder()
            .fromHubId(fromHubId)
            .toHubId(toHubId)
            .distance(distance)
            .duration(duration)
            .build();
    }

}
