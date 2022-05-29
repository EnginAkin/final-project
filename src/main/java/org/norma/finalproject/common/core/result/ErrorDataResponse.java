package org.norma.finalproject.common.core.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDataResponse<T> extends GeneralResponse {
    private final T data;

    public ErrorDataResponse(T data) {
        super("Unsuccessfull", false);
        this.data = data;
    }

    public ErrorDataResponse(T data, String message) {
        super(message, false);
        this.data = data;
    }
}
