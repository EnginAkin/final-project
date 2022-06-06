package org.norma.finalproject.account.core.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class AccountActivityResponse {

    private String crossAccount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String senderName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiverName;

    private BigDecimal amount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    private BigDecimal availableBalance;

    private Date date;
}
