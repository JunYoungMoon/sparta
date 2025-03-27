package takeoff.logistics_service.msa.user.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import takeoff.logistics_service.msa.common.exception.code.ErrorCode;

@Getter
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("USR_001", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USR_002", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS("USR_003", "이미 사용 중인 사용자 이름입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("USR_004", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    ALREADY_DELETED("USR_005", "이미 삭제된 사용자입니다.", HttpStatus.CONFLICT),
    DELIVERY_MANAGER_NOT_FOUND("USR_006", "배송 관리자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MAX_DELIVERY_MANAGER_EXCEEDED("USR_007", "배송 담당자는 허브당 최대 10명까지 등록 가능합니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_MANAGER_TYPE("USR_008", "지원하지 않는 배송 관리자 타입입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final int status;

    UserErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status.value();
    }
}
