package com.example.cryptoapp.crypto.wallet.coin.user_coin;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCoinId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "coin_id")
    private Long coinId;
}
