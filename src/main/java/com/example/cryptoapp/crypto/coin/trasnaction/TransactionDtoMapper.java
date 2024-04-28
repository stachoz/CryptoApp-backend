package com.example.cryptoapp.crypto.coin.trasnaction;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import com.example.cryptoapp.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionDtoMapper {

    private final CoinRepository coinRepository;

    public TransactionDtoMapper(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Transaction map(AddTransactionDto dto, Optional<Transaction> lastCoinTransaction, User currentUser){
        Transaction transaction = new Transaction();
        TransactionType transactionType = dto.getType();
        BigDecimal quantity = dto.getQuantity();
        BigDecimal price = dto.getPrice();
        String coinSymbol = dto.getSymbol();
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTransactionType(transactionType);
        Coin coin = coinRepository.findCoinByName(coinSymbol).orElseGet(() -> coinRepository.save(new Coin(coinSymbol)));
        transaction.setCoin(coin);
        transaction.setUser(currentUser);

        BigDecimal value = price.multiply(quantity);
        if(lastCoinTransaction.isPresent()){
            Transaction lastTransaction = lastCoinTransaction.get();
            BigDecimal totalAmountOld = lastTransaction.getTotalAmount();
            BigDecimal roiOld = lastTransaction.getRoi();
            BigDecimal updatedTotalAmount, updatedRoi;
            if(transactionType == TransactionType.SELL){
                updatedTotalAmount = totalAmountOld.subtract(quantity);
                updatedRoi = roiOld.add(value);
            } else {
                updatedTotalAmount = totalAmountOld.add(quantity);
                updatedRoi = roiOld.subtract(value);
            }
            transaction.setTotalAmount(updatedTotalAmount);
            transaction.setRoi(updatedRoi);
        } else {
            transaction.setTotalAmount(quantity);
            transaction.setRoi(value.negate());
        }
        return transaction;
    }

    public TransactionDto map(Transaction transaction){
        TransactionDto dto = new TransactionDto();
        dto.setQuantity(transaction.getQuantity());
        dto.setPrice(transaction.getPrice());
        dto.setType(transaction.getTransactionType());
        dto.setTotalAmount(transaction.getTotalAmount());
        dto.setRoi(transaction.getRoi());
        dto.setSymbol(transaction.getCoin().getName());
        return dto;
    }
}
