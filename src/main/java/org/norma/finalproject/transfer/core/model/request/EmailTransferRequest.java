package org.norma.finalproject.transfer.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.transfer.entity.enums.TransferType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class EmailTransferRequest {

    @NotNull
    private String fromAccountIban;
    @NotNull
    private String toEmail;
    @NotNull
    private BigDecimal amount;
    private String description;
    private TransferType transferType;

}
