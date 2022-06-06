package org.norma.finalproject.common.core.exception;

import org.norma.finalproject.common.core.result.ErrorDataResponse;
import org.norma.finalproject.common.core.result.GeneralErrorResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.customer.core.exception.LoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginFailedException.class)
    public GeneralResponse handleLoginFailedException(LoginFailedException loginFailedException) {
        return new GeneralErrorResponse(loginFailedException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    public GeneralResponse handleExchangeApiException(HttpClientErrorException httpClientErrorException) {
        return new GeneralErrorResponse(httpClientErrorException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public GeneralResponse handleNotFoundException(NotFoundException notFoundException) {
        return new GeneralErrorResponse(notFoundException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public GeneralResponse handleBusinessException(BusinessException businessException) {
        return new GeneralErrorResponse(businessException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public GeneralResponse handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new GeneralErrorResponse(illegalArgumentException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public GeneralResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        return new GeneralErrorResponse(httpMessageNotReadableException.getLocalizedMessage());

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public GeneralResponse handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> validationErrors = new HashMap<String, String>();

        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ErrorDataResponse<>(validationErrors);
    }

    @ExceptionHandler
    public GeneralResponse handleConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = new ArrayList<>(exception.getConstraintViolations()).get(0).getMessage();
        return new ErrorDataResponse<>(errorMessage);
    }


}
