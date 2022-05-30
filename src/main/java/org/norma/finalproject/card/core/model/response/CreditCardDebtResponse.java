package org.norma.finalproject.card.core.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreditCardDebtResponse {

    private BigDecimal currentTermDebt;
    private BigDecimal lastExtractDebt;
    private BigDecimal totalDebt;


}
