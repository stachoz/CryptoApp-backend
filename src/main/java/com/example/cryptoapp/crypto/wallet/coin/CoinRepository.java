package com.example.cryptoapp.crypto.wallet.coin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findCoinByName(String symbol);
    boolean existsByName(String symbol);
}
