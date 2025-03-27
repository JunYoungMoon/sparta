package takeoff.logistics_service.msa.product.product.domain.repository.search;

import java.util.List;

public record PaginatedResult<T>(
	List<T> content, int page, int size, Long totalElements, int totalPages) {
}

