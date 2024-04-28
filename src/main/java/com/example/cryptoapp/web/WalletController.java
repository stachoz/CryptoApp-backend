package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.coin.trasnaction.TransactionDto;
import com.example.cryptoapp.crypto.WalletService;
import com.example.cryptoapp.crypto.coin.trasnaction.AddTransactionDto;
import com.example.cryptoapp.crypto.coin.user_coin.UserCoinDtoInfo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserCoinDtoInfo>> getWallet(){
        List<UserCoinDtoInfo> userCoinsDto = walletService.getCurrentUserCoins();
        return new ResponseEntity<>(userCoinsDto, HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody @Valid AddTransactionDto dto){
        TransactionDto savedTransaction = walletService.addTransaction(dto);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }
}
