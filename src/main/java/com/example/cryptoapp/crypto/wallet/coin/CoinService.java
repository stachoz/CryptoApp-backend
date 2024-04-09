package com.example.cryptoapp.crypto.wallet.coin;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinService {
    public List<String> getBaseCoinsNames() {
        return Arrays.stream(BaseCoin.values())
                .map(baseCoin -> baseCoin.toString().toLowerCase())
                .collect(Collectors.toList());
    }
}