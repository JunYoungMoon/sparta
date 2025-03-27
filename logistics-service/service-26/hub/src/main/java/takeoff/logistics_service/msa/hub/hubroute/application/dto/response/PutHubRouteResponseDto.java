package takeoff.logistics_service.msa.hub.hubroute.application.dto.response;

import java.util.UUID;
import lombok.Builder;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Distance;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Duration;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.HubRoute;

/**
 * @author : hanjihoon
 * @Date : 2025. 03. 15.
 */
@Builder
public record PutHubRouteResponseDto(UUID hubRouteId,
                                     UUID fromHubId,
                                     UUID toHubId,
                                     Distance distance,
                                     Duration duration) {

    public static PutHubRouteResponseDto from(HubRoute hubRoute) {
        return PutHubRouteResponseDto.builder()
            .hubRouteId(hubRoute.getId())
            .fromHubId(hubRoute.getFromHubId())
            .toHubId(hubRoute.getToHubId())
            .distance(hubRoute.getDistance())
            .duration(hubRoute.getDuration())
            .build();
    }
}
