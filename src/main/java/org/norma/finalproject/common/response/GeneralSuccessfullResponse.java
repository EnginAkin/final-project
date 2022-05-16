package org.norma.finalproject.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralSuccessfullResponse extends GeneralResponse {
    public GeneralSuccessfullResponse(String message) {
        super(message, true);
    }

}
