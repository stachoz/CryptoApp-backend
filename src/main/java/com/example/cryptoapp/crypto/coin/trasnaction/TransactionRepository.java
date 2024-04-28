package com.example.cryptoapp.crypto.coin.trasnaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findFirstTransactionByUserIdAndAndCoin_NameOrderByTimeAddedDesc(Long userId, String coinName);

    @Query(nativeQuery = true, value =
            "select distinct on (coin_id) * from transaction where user_id = :userId order by coin_id, time_added desc"
    )
    List<Transaction> getUserLastDistinctCoinsTransaction(@Param("userId") Long userId);

}