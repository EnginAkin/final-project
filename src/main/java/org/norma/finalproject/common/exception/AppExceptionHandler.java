package org.norma.finalproject.common.exception;

import org.norma.finalproject.common.response.GeneralErrorResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class AppExceptionHandler {


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginFailedException.class)
    public GeneralResponse handleIllegalArgumentException(LoginFailedException loginFailedException) {
        return new GeneralErrorResponse(loginFailedException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    public GeneralResponse handleExchangeApiException(HttpClientErrorException httpClientErrorException) {
        return new GeneralErrorResponse(httpClientErrorException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public GeneralResponse handleBusinessException(BusinessException businessException) {
        return new GeneralErrorResponse(businessException.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public GeneralResponse handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new GeneralErrorResponse(illegalArgumentException.getMessage());
    }



}
