package com.example.cryptoapp.crypto.wallet.coin.user_coin;

import com.example.cryptoapp.crypto.wallet.coin.Coin;
import com.example.cryptoapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
public class UserCoin {

    @EmbeddedId
    private UserCoinId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("coinId")
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @NotNull
    private BigDecimal quantity;
}
