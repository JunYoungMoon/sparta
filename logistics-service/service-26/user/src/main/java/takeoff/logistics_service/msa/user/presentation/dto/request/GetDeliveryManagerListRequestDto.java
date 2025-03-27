package takeoff.logistics_service.msa.user.presentation.dto.request;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import takeoff.logistics_service.msa.user.domain.service.DeliveryManagerSearchCondition;
import takeoff.logistics_service.msa.user.domain.vo.DeliveryManagerType;

@Builder
public record GetDeliveryManagerListRequestDto(
        String hubId,
        DeliveryManagerType deliveryManagerType,
        Integer page,
        Integer size
) {
    public DeliveryManagerSearchCondition toCondition() {
        return new DeliveryManagerSearchCondition(hubId, deliveryManagerType);
    }

    public Pageable toPageable() {
        int defaultPage = (page != null) ? page : 0;
        int defaultSize = (size != null) ? size : 10;
        List<Integer> allowedSizes = List.of(10, 30, 50);

        if (!allowedSizes.contains(defaultSize)) {
            defaultSize = 10;
        }

        return PageRequest.of(defaultPage, defaultSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
