package me.rocky.auth.exception;

import me.rocky.common.result.Result;
import me.rocky.common.result.ResultCode;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    /**
     * 用户名和密码异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidGrantException.class)
    public Result handleInvalidGrantException(InvalidGrantException e) {
        return Result.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
    }


    /**
     * 账户异常(禁用、锁定、过期)
     *
     * @param e
     * @return
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public Result handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return Result.failed(e.getMessage());
    }

}
