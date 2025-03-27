package takeoff.logistics_service.msa.hub.hubroute.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import takeoff.logistics_service.msa.hub.hubroute.application.dto.response.GetHubRouteResponseDto;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Distance;
import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Duration;

/**
 * @author : hanjihoon
 * @Date : 2025. 03. 15.
 */
@Builder
public record GetHubRouteResponse(UUID hubRouteId,
                                  UUID fromHubId,
                                  UUID toHubId,
                                  Distance distance,
                                  Duration duration) {

    public static GetHubRouteResponse from(GetHubRouteResponseDto getHubRouteResponseDto) {
        return GetHubRouteResponse.builder()
            .hubRouteId(getHubRouteResponseDto.hubRouteId())
            .fromHubId(getHubRouteResponseDto.fromHubId())
            .toHubId(getHubRouteResponseDto.toHubId())
            .distance(getHubRouteResponseDto.distance())
            .duration(getHubRouteResponseDto.duration())
            .build();
    }
}
