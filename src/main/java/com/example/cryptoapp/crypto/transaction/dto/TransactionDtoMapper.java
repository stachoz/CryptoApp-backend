package com.example.cryptoapp.crypto.transaction.dto;

import com.example.cryptoapp.crypto.transaction.TransactionType;
import com.example.cryptoapp.crypto.transaction.coin.Coin;
import com.example.cryptoapp.crypto.transaction.coin.CoinRepository;
import com.example.cryptoapp.crypto.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionDtoMapper {
    private final CoinRepository coinRepository;

    public TransactionDtoMapper(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Transaction map(TransactionDto dto){
        String coinName = dto.getCoinName();
        Coin coin = coinRepository.findCoinByName(coinName).orElse(new Coin(coinName));
        Transaction transaction = new Transaction();
        transaction.setPrice(dto.getPrice());
        transaction.setQuantity(dto.getQuantity());
        transaction.setCoin(coin);
        transaction.setType(TransactionType.valueOf(dto.getTransactionType()));
        return transaction;
    }
}
