package takeoff.logistics_service.msa.user.presentation.dto.response;

import lombok.Builder;
import takeoff.logistics_service.msa.user.domain.entity.User;

@Builder
public record PostSignupResponseDto(
        Long userId,
        String username,
        String slackEmail
) {

    public static PostSignupResponseDto from(User user) {
        return PostSignupResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .slackEmail(user.getSlackEmail())
                .build();
    }
}
