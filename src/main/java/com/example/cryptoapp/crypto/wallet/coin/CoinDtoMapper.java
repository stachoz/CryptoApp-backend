package com.example.cryptoapp.crypto.wallet.coin;

import com.example.cryptoapp.crypto.wallet.coin.user_coin.UserCoin;
import org.springframework.stereotype.Service;

@Service
public class CoinDtoMapper {

    public UserCoin map(CoinDto dto){
        UserCoin userCoin = new UserCoin();
        userCoin.setQuantity(dto.getQuantity());
        return userCoin;
    }
}
