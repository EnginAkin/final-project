package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.repository.CreditCardRepository;
import org.norma.finalproject.card.service.CreditCardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public Optional<CreditCard> findCreditCardByCreditCardNumber(String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public Optional<CreditCard> findCreditCardById(long id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public void delete(CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    @Override
    public List<CreditCard> findAllCreditCardsCutoffDateInToday() {
        return creditCardRepository.findAllCutoffDateInToday();
    }
}
