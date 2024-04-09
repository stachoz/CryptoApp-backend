package com.example.cryptoapp.web;

import com.example.cryptoapp.crypto.wallet.coin.CoinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("")
    public List<String> getBaseCoins(){
        return coinService.getBaseCoinsNames();
    }

}
