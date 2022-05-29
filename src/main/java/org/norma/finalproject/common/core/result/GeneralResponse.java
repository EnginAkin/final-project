package org.norma.finalproject.common.core.result;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {
    private String message;
    private Boolean isSuccessful;

    public GeneralResponse(String message) {
        this.message = message;
    }
}
