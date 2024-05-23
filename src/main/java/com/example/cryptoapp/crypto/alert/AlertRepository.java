package com.example.cryptoapp.crypto.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    boolean existsByAlertPriceAndUserIdAndCoin_Name(BigDecimal alertPrice, Long userId, String coinSymbol);
    List<Alert> findAllByUserId(Long userId);
}
