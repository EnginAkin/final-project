package org.norma.finalproject.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
