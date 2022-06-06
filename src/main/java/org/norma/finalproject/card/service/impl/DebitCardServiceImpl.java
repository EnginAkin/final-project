package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.repository.DebitCardRepository;
import org.norma.finalproject.card.service.DebitCardService;
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
public class DebitCardServiceImpl implements DebitCardService {
    private final DebitCardRepository debitCardRepository;

    @Override
    public DebitCard save(DebitCard card) {
        return debitCardRepository.save(card);
    }


    @Override
    public List<DebitCard> getAllCustomersDebitCards(long customerID) {
        return debitCardRepository.findAllByCheckingAccount_Customer_Id(customerID);
    }

    @Override
    public Optional<DebitCard> findDebitCardWithCustomerIDAndCardID(long customerID, long debitCardId) {
        return debitCardRepository.findDebitCardByCheckingAccount_Customer_IdAndId(customerID, debitCardId);
    }


    @Override
    public Optional<DebitCard> findDebitCardWithCardNumber(String cardNumber) {
        return debitCardRepository.findDebitCardByCardNumber(cardNumber);
    }

    @Override
    public boolean existsDebitCardByCheckingAccountId(long checkingAccountID) {
        return debitCardRepository.existsByCheckingAccount_Id(checkingAccountID);
    }

    @Override
    public Optional<DebitCard> findByParentCheckingAccount(long parentId) {
        return debitCardRepository.findDebitCardByCheckingAccount_Id(parentId);
    }

    @Override
    public void deleteByCheckingAccountId(long parentId) {
        Optional<DebitCard> optionalDebitCard = findByParentCheckingAccount(parentId);
        if (optionalDebitCard.isEmpty()) {
            return;
        }
        debitCardRepository.delete(optionalDebitCard.get());
    }

    @Override
    public void delete(DebitCard debitCard) {
        debitCardRepository.delete(debitCard);
    }
}
