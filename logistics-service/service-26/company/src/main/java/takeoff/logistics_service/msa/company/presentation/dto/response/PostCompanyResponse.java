package takeoff.logistics_service.msa.company.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import takeoff.logistics_service.msa.company.application.dto.response.PostCompanyResponseDto;

@Builder
public record PostCompanyResponse(
	UUID companyId, String companyName, String companyType,
	UUID hubId, String address, LocalDateTime createdAt, LocalDateTime updatedAt) {

	public static PostCompanyResponse from (PostCompanyResponseDto responseDto) {
		return PostCompanyResponse.builder()
			.companyId(responseDto.companyId())
			.companyName(responseDto.companyName())
			.companyType(responseDto.companyType())
			.hubId(responseDto.hubId())
			.address(responseDto.address())
			.createdAt(responseDto.createdAt())
			.updatedAt(responseDto.updatedAt())
			.build();
	}
}
