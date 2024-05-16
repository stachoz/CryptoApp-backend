package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.WalletService;
import com.example.cryptoapp.crypto.coin.trasnaction.AddTransactionDto;
import com.example.cryptoapp.crypto.coin.trasnaction.TransactionDto;
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
    @PostMapping("/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody @Valid AddTransactionDto dto){
        TransactionDto savedTransaction = walletService.addTransaction(dto);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }
    @PutMapping("/transactions/lastOnCoins/{symbol}")
    public ResponseEntity<?> updateLastTransactionOnCoin(@RequestBody @Valid AddTransactionDto dto, @PathVariable String symbol){
        TransactionDto updatedTransaction = walletService.updateLastCoinTransaction(dto, symbol);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }
    @GetMapping("/transactions/lastOnCoins")
    public ResponseEntity<List<TransactionDto>> getWallet(){
        List<TransactionDto> userCoinsDto = walletService.getCurrentUserLastTransactionOnUniqueCoins();
        return new ResponseEntity<>(userCoinsDto, HttpStatus.OK);
    }

    @DeleteMapping("/transactions/lastOnCoins/{symbol}")
    public ResponseEntity<?> deleteLastUserTransactionOnCoin(@PathVariable String symbol){
        walletService.deleteLastUserTransactionOnCoin(symbol);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
