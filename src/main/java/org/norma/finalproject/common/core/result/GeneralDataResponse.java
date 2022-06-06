package org.norma.finalproject.common.core.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneralDataResponse<T> extends GeneralResponse {
    private final T data;

    public GeneralDataResponse(T data) {
        super("successfull", true);
        this.data = data;
    }

    public GeneralDataResponse(T data, String message) {
        super(message, true);
        this.data = data;
    }
}
