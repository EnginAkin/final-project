package org.norma.finalproject.card.core.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateDebitCardRequest {

    @NotNull
    private long parentCheckingAccountId;

    private String password;

}
