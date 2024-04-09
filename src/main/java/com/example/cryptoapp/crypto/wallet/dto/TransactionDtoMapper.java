package com.example.cryptoapp.crypto.wallet.dto;

import com.example.cryptoapp.crypto.wallet.TransactionType;
import com.example.cryptoapp.crypto.wallet.coin.Coin;
import com.example.cryptoapp.crypto.wallet.coin.CoinRepository;
import com.example.cryptoapp.crypto.wallet.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionDtoMapper {
    private final CoinRepository coinRepository;

    public TransactionDtoMapper(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Transaction map(TransactionDto dto){
        String coinName = dto.getSymbol();
        Coin coin = coinRepository.findCoinByName(coinName).orElse(new Coin(coinName));
        Transaction transaction = new Transaction();
        transaction.setPrice(dto.getPrice());
        transaction.setQuantity(dto.getQuantity());
        transaction.setCoin(coin);
        transaction.setType(TransactionType.valueOf(dto.getTransactionType()));
        return transaction;
    }
}
