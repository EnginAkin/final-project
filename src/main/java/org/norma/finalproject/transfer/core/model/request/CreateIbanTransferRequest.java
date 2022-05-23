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
// 1. hesap iban no TR3300066107021978911206485 // 40 tl

// 2. iban no TR3300066102871903576532348 // 10 tl