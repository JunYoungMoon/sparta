package takeoff.logistics_service.msa.auth.application.exception;

import takeoff.logistics_service.msa.common.exception.BusinessException;
import takeoff.logistics_service.msa.common.exception.code.ErrorCode;

public class AuthBusinessException extends BusinessException {

    private AuthBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static AuthBusinessException from(ErrorCode errorCode) {
        return new AuthBusinessException(errorCode);
    }
}
