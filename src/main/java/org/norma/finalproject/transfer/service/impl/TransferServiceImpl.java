package org.norma.finalproject.transfer.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.transfer.entity.Transfer;
import org.norma.finalproject.transfer.repository.TransferRepository;
import org.norma.finalproject.transfer.service.TransferService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;

    @Override
    public void save(Transfer transfer) {
        transferRepository.save(transfer);
    }
}
