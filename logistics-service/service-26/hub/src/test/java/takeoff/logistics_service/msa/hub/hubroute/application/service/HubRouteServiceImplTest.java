//package takeoff.logistics_service.msa.hub.hubroute.application.service;
//
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.UUID;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.DistanceDto;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.DurationDto;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.client.HubClient;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.request.HubIdsDto;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.request.PostHubRouteRequestDto;
//import takeoff.logistics_service.msa.hub.hubroute.application.dto.response.PostHubRouteResponseDto;
//import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Distance;
//import takeoff.logistics_service.msa.hub.hubroute.domain.entity.Duration;
//import takeoff.logistics_service.msa.hub.hubroute.domain.entity.HubRoute;
//import takeoff.logistics_service.msa.hub.hubroute.domain.repository.HubRouteRepository;
//
///**
// * @author : hanjihoon
// * @Date : 2025. 03. 20.
// */
//@DisplayName("HubRoute Service Test")
//@ExtendWith(MockitoExtension.class)
//class HubRouteServiceImplTest {
//
//    @Mock
//    private HubRouteRepository hubRouteRepository;
//
//    @Mock
//    private CacheManager cacheManager;
//
//    @Mock
//    private HubClient hubClient;
//
//    @InjectMocks
//    private HubRouteServiceImpl hubRouteServiceImpl;
//
//    @Mock
//    private Cache cache;
//
//    @BeforeEach
//    void setUp() {
//        // 캐시 설정
//        when(cacheManager.getCache("hubRoutes")).thenReturn(cache);
//    }
//
//
//    @DisplayName("createHubRoute 메서드 캐시 테스트")
//    @Test
//    void testCreateHubRouteWithCache() {
//        UUID fromHubId = UUID.randomUUID();
//        UUID toHubId = UUID.randomUUID();
//
//        PostHubRouteRequestDto requestDto = new PostHubRouteRequestDto(fromHubId, toHubId);
//
//        // 캐시가 비어있는지 확인
//        assertThat(cache.get("someKey")).isNull();  // 캐시가 비어 있어야 함
//
//        // 캐시되지 않음
//        PostHubRouteResponseDto responseDto = hubRouteServiceImpl.createHubRoute(requestDto);
//
//        // 캐시된 값을 반환
//        PostHubRouteResponseDto cachedResponseDto = hubRouteServiceImpl.createHubRoute(requestDto);
//
//        // 캐시된 값 반환
//        assertThat(responseDto).isEqualTo(cachedResponseDto);
//
//
//        assertThat(cache.get("someKey")).isNotNull();  // 캐시가 저장되었어야 함
//    }
//
//
//
//}