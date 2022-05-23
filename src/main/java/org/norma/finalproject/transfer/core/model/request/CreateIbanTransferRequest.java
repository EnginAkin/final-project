package org.norma.finalproject.transfer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.transfer.entity.enums.SendType;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateIbanTransferRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String description;
    private SendType sendType;
}
