package com.example.cryptoapp.crypto.coin.user_coin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class UserCoinDtoInfo {
    private String symbol;
    private BigDecimal roi;
    private BigDecimal totalAmount;
}