package takeoff.logistics_service.msa.user.presentation.common.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PaginationDto(int currentPage, int pageSize, int totalPages, long totalElements) {
    public static PaginationDto from(Page<?> page) {
        return PaginationDto.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
