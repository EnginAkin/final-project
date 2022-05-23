package org.norma.finalproject.transfer.core.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTransferRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
}
