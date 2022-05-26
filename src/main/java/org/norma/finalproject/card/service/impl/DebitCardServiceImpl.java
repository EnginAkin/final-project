package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.repository.DebitCardRepository;
import org.norma.finalproject.card.service.DebitCardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {
    private final DebitCardRepository debitCardRepository;

    @Override
    public DebitCard save(DebitCard card) {
        return debitCardRepository.save(card);
    }

    @Override
    public Optional<DebitCard> findById(long debitCardID) {
        return debitCardRepository.findById(debitCardID);
    }

    @Override
    public List<DebitCard> getAllCustomersDebitCards(long customerID) {
        return debitCardRepository.findAllByCheckingAccount_Customer_Id(customerID);
    }

    @Override
    public Optional<DebitCard> findDebitCardWithCustomerIDAndCardID(long customerID, long debitCardId) {
        return debitCardRepository.findDebitCardByCheckingAccount_Customer_IdAndId(customerID,debitCardId);
    }

    @Override
    public Optional<DebitCard> findDebitCardWithCustomerIDAndCardNumber(long customerID, String cardNumber) {
        return debitCardRepository.findDebitCardByCheckingAccount_Customer_IdAndCardNumber(customerID,cardNumber);
    }

    @Override
    public Optional<DebitCard> findDebitCardWithCardNumber(String cardNumber) {
        return debitCardRepository.findDebitCardByCardNumber(cardNumber);
    }

    @Override
    public boolean existsDebitCardByCheckingAccountId(long checkingAccountID) {
        return debitCardRepository.existsByCheckingAccount_Id(checkingAccountID);
    }
}
