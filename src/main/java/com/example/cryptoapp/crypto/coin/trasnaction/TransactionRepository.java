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

    @Query(nativeQuery = true, value = "select t.* from transaction t join public.coin c on c.id = t.coin_id where t.user_id = :userId " +
            "and c.name = :coinName order by t.time_added desc limit 1 offset 1")
    Optional<Transaction> findUserSecondTransactionOnCoin(@Param("userId") Long userId, @Param("coinName") String coinName);

    @Query(nativeQuery = true, value =
            "select distinct on (coin_id) * from transaction where user_id = :userId order by coin_id, time_added desc"
    )
    List<Transaction> getUserLastDistinctCoinsTransactions(@Param("userId") Long userId);
}