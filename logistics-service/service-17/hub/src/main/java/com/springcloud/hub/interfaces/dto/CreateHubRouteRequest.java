package com.springcloud.hub.interfaces.dto;

import java.util.UUID;

public record CreateHubRouteRequest(UUID startHubId, UUID goalHubId) {
}
