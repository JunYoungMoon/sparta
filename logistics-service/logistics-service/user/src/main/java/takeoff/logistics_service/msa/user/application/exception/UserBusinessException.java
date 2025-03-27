package takeoff.logistics_service.msa.user.application.exception;

import takeoff.logistics_service.msa.common.exception.BusinessException;
import takeoff.logistics_service.msa.common.exception.code.ErrorCode;

public class UserBusinessException extends BusinessException {

    private UserBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static UserBusinessException from(ErrorCode errorCode) {
        return new UserBusinessException(errorCode);
    }
}
