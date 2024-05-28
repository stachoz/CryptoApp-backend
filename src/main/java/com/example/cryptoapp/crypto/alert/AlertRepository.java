package com.example.cryptoapp.crypto.alert;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    boolean existsByAlertPriceAndUserIdAndCoin_Name(BigDecimal alertPrice, Long userId, String coinSymbol);
    List<Alert> findAllByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(nativeQuery = true, value = "select * from alert where alert.id = :id")
    Optional<Alert> findByIdWithLock(@Param("id") Long id);
}
