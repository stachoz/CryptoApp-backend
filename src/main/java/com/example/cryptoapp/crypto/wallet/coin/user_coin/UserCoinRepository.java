package com.example.cryptoapp.crypto.wallet.coin.user_coin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, UserCoinId> {
}
