package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.wallet.Transaction;
import com.example.cryptoapp.crypto.wallet.coin.CoinDto;
import com.example.cryptoapp.crypto.wallet.dto.TransactionDto;
import com.example.cryptoapp.crypto.wallet.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transactions")
    public Transaction addTransaction(@Valid @RequestBody TransactionDto dto){
        return walletService.addTransaction(dto);
    }

    @PostMapping("/coins")
    public ResponseEntity<?> addCoin(@Valid @RequestBody CoinDto coinDto){
        walletService.addCoin(coinDto);
        return ResponseEntity.ok().build();
    }
}
