package com.example.cryptoapp.crypto.coin.user_coin;

import com.example.cryptoapp.crypto.coin.trasnaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class UserCoinDtoInfoMapper {
    public UserCoinDtoInfo map(Transaction transaction) {
        return new UserCoinDtoInfo(
                transaction.getCoin().getName(),
                transaction.getRoi(),
                transaction.getTotalAmount()
        );
    }
}
