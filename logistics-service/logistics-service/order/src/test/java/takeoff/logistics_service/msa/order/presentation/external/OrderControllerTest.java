package takeoff.logistics_service.msa.order.presentation.external;


import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import takeoff.logistics_service.msa.common.domain.UserInfoDto;
import takeoff.logistics_service.msa.common.domain.UserRole;
import takeoff.logistics_service.msa.order.application.dto.PaginatedResultDto;
import takeoff.logistics_service.msa.order.application.dto.request.SearchOrderRequestDto;
import takeoff.logistics_service.msa.order.application.dto.response.SearchOrderItemResponseDto;
import takeoff.logistics_service.msa.order.application.dto.response.SearchOrderResponseDto;
import takeoff.logistics_service.msa.order.application.service.OrderService;
import takeoff.logistics_service.msa.order.domain.entity.Order;
import takeoff.logistics_service.msa.order.domain.entity.OrderId;
import takeoff.logistics_service.msa.order.domain.entity.OrderItem;
import takeoff.logistics_service.msa.order.presentation.dto.request.PatchOrderItemRequest;
import takeoff.logistics_service.msa.order.presentation.dto.request.PatchOrderRequest;
import takeoff.logistics_service.msa.order.presentation.dto.request.PostOrderItemRequest;
import takeoff.logistics_service.msa.order.presentation.dto.request.PostOrderRequest;
import takeoff.logistics_service.msa.order.presentation.dto.request.SearchOrderRequest;
import takeoff.logistics_service.msa.order.presentation.dto.response.PatchOrderResponse;
import takeoff.logistics_service.msa.order.presentation.dto.response.PostOrderResponse;

@WebMvcTest(OrderController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"})
class OrderControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private OrderService orderService;

  private final String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
      + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6M"
      + "TUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

  @Test
  @DisplayName("[Order][POST] 주문 생성 - 정상 호출")
  void 주문_정보를_입력하여_주문_생성() throws Exception {
    // given
    UUID product1Id = createRandomUUID("product1");
    UUID product2Id = createRandomUUID("product2");

    List<PostOrderItemRequest> orderItemsRequest = List.of(
        new PostOrderItemRequest(
            product1Id,
            1
        ),
        new PostOrderItemRequest(
            product2Id,
            2
        )
    );

    UUID supplier1 = createRandomUUID("supplier1");
    UUID company1 = createRandomUUID("company1");
    long customerId = 1L;
    String address = "대전 서구 둔산로 100";
    String requestNotes = "요청 사항 입니다.";

    PostOrderRequest request = new PostOrderRequest(
        supplier1,
        company1,
        customerId,
        orderItemsRequest,
        address,
        requestNotes
    );

    UUID order1 = createRandomUUID("order1");

    // 도메인 객체 모킹
    Order mockOrder = mock(Order.class);
    OrderItem mockOrderItem1 = mock(OrderItem.class);
    OrderItem mockOrderItem2 = mock(OrderItem.class);
    OrderId orderId = mock(OrderId.class);

    // 모의 객체 동작 설정
    when(orderId.getOrderId()).thenReturn(order1);
    when(mockOrder.getId()).thenReturn(orderId);
    when(mockOrder.getSupplierId()).thenReturn(supplier1);
    when(mockOrder.getCustomerId()).thenReturn(customerId);
    when(mockOrder.getAddress()).thenReturn(address);
    when(mockOrder.getRequestNotes()).thenReturn(requestNotes);

    when(mockOrderItem1.getProductId()).thenReturn(product1Id);
    when(mockOrderItem1.getQuantity()).thenReturn(1);
    when(mockOrderItem2.getProductId()).thenReturn(product2Id);
    when(mockOrderItem2.getQuantity()).thenReturn(2);

    List<OrderItem> orderItems = List.of(mockOrderItem1, mockOrderItem2);
    when(mockOrder.getOrderItems()).thenReturn(orderItems);

    // 응답 생성
    PostOrderResponse response = PostOrderResponse.from(mockOrder);

    // 서비스 응답 설정
    given(orderService.saveOrder(any(PostOrderRequest.class))).willReturn(response);

    // when && then
    mvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", "1")
            .header("X-User-Role", "MASTER_ADMIN")
            .header(HttpHeaders.AUTHORIZATION, token)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.orderId").value(order1.toString()))
        .andExpect(jsonPath("$.supplierId").value(supplier1.toString()))
        .andExpect(jsonPath("$.customerId").value(customerId))
        .andExpect(jsonPath("$.address").value(address))
        .andExpect(jsonPath("$.requestNotes").value(requestNotes))
        .andDo(
            document("주문 생성",
                preprocessRequest(Preprocessors.prettyPrint()),
                preprocessResponse(Preprocessors.prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                    .tag("Order-External")
                    .summary("주문 생성")
                    .description("주문을 생성하는 외부 API 입니다.")
                    .build())
            )
        );

    then(orderService).should().saveOrder(any(PostOrderRequest.class));
  }

  @Test
  @DisplayName("[Order][Patch] 주문 수정 - 정상 호출")
  void 관리자나_배송담당자는_주문_상태를_수정한다() throws Exception {
    // given
    UUID product1Id = createRandomUUID("product1");
    UUID product2Id = createRandomUUID("product2");

    List<PatchOrderItemRequest> orderItemsRequest = List.of(
        new PatchOrderItemRequest(product1Id, 3),
        new PatchOrderItemRequest(product2Id, 4)
    );

    PatchOrderRequest request = new PatchOrderRequest(orderItemsRequest);
    UUID orderId = createRandomUUID("order1");

    // 도메인 객체 모킹
    Order mockOrder = mock(Order.class);
    OrderItem mockOrderItem1 = mock(OrderItem.class);
    OrderItem mockOrderItem2 = mock(OrderItem.class);

    when(mockOrderItem1.getProductId()).thenReturn(product1Id);
    when(mockOrderItem1.getQuantity()).thenReturn(3);
    when(mockOrderItem2.getProductId()).thenReturn(product2Id);
    when(mockOrderItem2.getQuantity()).thenReturn(4);

    List<OrderItem> orderItems = List.of(mockOrderItem1, mockOrderItem2);
    when(mockOrder.getOrderItems()).thenReturn(orderItems);

    PatchOrderResponse response = PatchOrderResponse.from(mockOrder);
    given(orderService.updateOrder(any(PatchOrderRequest.class),
        eq(orderId))).willReturn(response);

    // when
    mvc.perform(patch("/api/v1/orders/{orderId}", orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", "1")
            .header("X-User-Role", "MASTER_ADMIN")
            .header(HttpHeaders.AUTHORIZATION, token)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(
            document("주문 수정",
                preprocessRequest(Preprocessors.prettyPrint()),
                preprocessResponse(Preprocessors.prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                    .tag("Order-External")
                    .summary("주문 수량 수정")
                    .description("주문 수량을 수정하는 외부 API 입니다.")
                    .requestFields(
                        fieldWithPath("orderItems[].productId").description("상품 ID"),
                        fieldWithPath("orderItems[].quantity").description("수량")
                    )
                    .build())
            )
        );
    // then
    then(orderService).should().updateOrder(any(PatchOrderRequest.class), eq(orderId));
  }

  @Test
  @DisplayName("[Order][Delete] 주문 삭제 = 정상 호출")
  void 관리지가_주문을_삭제할_수_있다() throws Exception {
    // given
    UUID orderId = createRandomUUID("order1");
    Long userId = 1L;

    // when
    UserInfoDto userInfoDto = new UserInfoDto(userId, UserRole.MASTER_ADMIN);

    mvc.perform(delete("/api/v1/orders/{orderId}", orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", userId.toString())
            .header("X-User-Role", "MASTER_ADMIN")
            .requestAttr("userInfoDto", userInfoDto)) // 인증 정보 속성 설정
        .andExpect(status().isOk())
        .andDo(
            document("주문 삭제",
                preprocessRequest(Preprocessors.prettyPrint()),
                preprocessResponse(Preprocessors.prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                    .tag("Order-External")
                    .summary("주문 삭제")
                    .description("주문을 삭제하는 외부 API 입니다.")
                    .pathParameters(
                        parameterWithName("orderId").description("삭제할 주문 ID")
                    )
                    .build())
            )
        );

    // then
    then(orderService).should().deleteOrder(eq(orderId), eq(userId));
  }

  @Test
  @DisplayName("[Order][GET] 주문 조회 - 정상 호출")
  void 주문을_검색할_수_있다() throws Exception {
    // given
    UUID orderId = createRandomUUID("order1");
    UUID supplierId = createRandomUUID("supplier1");
    UUID hubId = createRandomUUID("hub1");
    Long userId = 1L;

    // 검색 결과로 반환될 객체 생성
    SearchOrderItemResponseDto orderItemDto1 = new SearchOrderItemResponseDto(
        createRandomUUID("product1"),
        2
    );

    SearchOrderItemResponseDto orderItemDto2 = new SearchOrderItemResponseDto(
        createRandomUUID("product2"),
        3
    );

    LocalDateTime now = LocalDateTime.now();

    SearchOrderResponseDto responseDto = new SearchOrderResponseDto(
        orderId,
        supplierId,
        List.of(orderItemDto1, orderItemDto2),
        1L,
        "대전 서구 둔산로 100",
        "요청 사항입니다.",
        now,
        userId,
        now,
        userId
    );

    PaginatedResultDto<SearchOrderResponseDto> resultDto = new PaginatedResultDto<>(
        List.of(responseDto),
        0,
        10,
        1,
        1
    );

    SearchOrderRequest request = new SearchOrderRequest(
        supplierId,
        hubId,
        now.minusDays(7),
        now,
        false,
        "createdAt",
        0,
        10
    );

    UserInfoDto userInfoDto = new UserInfoDto(userId, UserRole.MASTER_ADMIN);

    // when
    given(orderService.searchOrder(any(SearchOrderRequestDto.class), eq(userId),
        eq(UserRole.MASTER_ADMIN)))
        .willReturn(resultDto);

    // then
    mvc.perform(get("/api/v1/orders/search")
            .param("supplierId", supplierId.toString())
            .param("hubId", hubId.toString())
            .param("startDate", request.startDate().toString())
            .param("endDate", request.endDate().toString())
            .param("isAsc", request.isAsc().toString())
            .param("sortBy", request.sortBy())
            .param("page", request.page().toString())
            .param("size", request.size().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-User-Id", userId.toString())
            .header("X-User-Role", "MASTER_ADMIN")
            .header(HttpHeaders.AUTHORIZATION, token)
            .requestAttr("userInfoDto", userInfoDto)) // 인증 정보 속성 설정
        .andExpect(status().isOk())
        .andDo(
            document("주문 검색",
                preprocessRequest(Preprocessors.prettyPrint()),
                preprocessResponse(Preprocessors.prettyPrint()),
                resource(ResourceSnippetParameters.builder()
                    .tag("Order-External")
                    .summary("주문 검색")
                    .description("주문을 검색하는 외부 API 입니다.")
                    .queryParameters(parameterWithName("supplierId").description("생산업체의 주문 필터링"),
                        parameterWithName("hubId").description("허브 관리자가 주문을 조회할 때 사용"),
                        parameterWithName("startDate").description("주문 시작일"),
                        parameterWithName("endDate").description("주문 종료일"),
                        parameterWithName("isAsc").description("정렬 방향"),
                        parameterWithName("sortBy").description("정렬 기준"),
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 크기")
                    )
                    .build()
                )
            )
        );

    then(orderService).should().searchOrder(any(SearchOrderRequestDto.class), eq(userId),
        eq(UserRole.MASTER_ADMIN));
  }

  private UUID createRandomUUID(String seed) {
    return UUID.nameUUIDFromBytes(seed.getBytes());
  }
}
