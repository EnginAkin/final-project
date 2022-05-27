package org.norma.finalproject.transfer.core.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.norma.finalproject.transfer.entity.enums.TransferType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IbanTransferRequest {
    private String fromIban;
    private String toIban;
    private BigDecimal amount;
    private String description;
    private TransferType transferType;
}
