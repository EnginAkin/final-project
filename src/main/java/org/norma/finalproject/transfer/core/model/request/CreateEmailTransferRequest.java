package org.norma.finalproject.transfer.core.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateEmailTransferRequest {
    private String fromIban;
    private String toEmail;
    private BigDecimal amount;
}
// 1. hesap iban no TR3300066107021978911206485 // 40 tl

// 2. iban no TR3300066102871903576532348 // 10 tl