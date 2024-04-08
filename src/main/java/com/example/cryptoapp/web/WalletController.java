package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.transaction.Transaction;
import com.example.cryptoapp.crypto.transaction.dto.TransactionDto;
import com.example.cryptoapp.crypto.transaction.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final TransactionService transactionService;

    public WalletController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public Transaction addTransaction(@Valid @RequestBody TransactionDto dto){
        return transactionService.addTransaction(dto);
    }
}
