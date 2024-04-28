package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {
    private final WalletService walletService;

    public CoinController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCoins(){
        List<String> coins = walletService.getCoins();
        return new ResponseEntity<>(coins, HttpStatus.OK);
    }
}
