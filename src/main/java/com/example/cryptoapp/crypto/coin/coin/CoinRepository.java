package com.example.cryptoapp.crypto.coin.coin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findCoinByName(String symbol);
    boolean existsByName(String symbol);
}
