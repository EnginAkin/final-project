package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.repository.CreditCardRepository;
import org.norma.finalproject.card.service.CreditCardService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;
    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
}
