package org.norma.finalproject.common.core.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralErrorResponse extends GeneralResponse {
    public GeneralErrorResponse(String message) {
        super(message, false);
    }

}
