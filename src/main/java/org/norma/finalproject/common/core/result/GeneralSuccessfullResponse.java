package org.norma.finalproject.common.core.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralSuccessfullResponse extends GeneralResponse {
    public GeneralSuccessfullResponse(String message) {
        super(message, true);
    }

}
