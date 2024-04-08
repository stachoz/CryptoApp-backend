package com.example.cryptoapp.crypto.transaction;

import com.example.cryptoapp.crypto.transaction.dto.TransactionDto;
import com.example.cryptoapp.crypto.transaction.dto.TransactionDtoMapper;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionDtoMapper transactionDtoMapper;
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionDtoMapper transactionDtoMapper, TransactionRepository transactionRepository) {
        this.transactionDtoMapper = transactionDtoMapper;
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransaction(TransactionDto dto){
        Transaction transaction = transactionDtoMapper.map(dto);
        return transactionRepository.save(transaction);
    }
}
