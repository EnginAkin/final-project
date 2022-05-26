package org.norma.finalproject.card.service;

import org.norma.finalproject.card.entity.enums.CreditRateType;

import java.math.BigDecimal;

public interface CreditRateService {
    CreditRateType getCreditRate(BigDecimal income);// gelire göre kredi puanı getiren servis.
}
