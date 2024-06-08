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
        transaction.setUser(currentUser);
        setTransactionValues(transaction, lastCoinTransaction, dto);
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
        dto.setDate(transaction.getTimeAdded());
        return dto;
    }

    public void mapDtoToExistingTransaction(AddTransactionDto dto, Transaction toUpdate, Optional<Transaction> previousTransaction){
        setTransactionValues(toUpdate, previousTransaction, dto);
    }

    /**
     *  Sets the values for the given transaction based on the provided DTO and the last transaction of the same coin.
     *  This method updates the transaction's quantity, price, type, associated coin, total amount, and ROI (Return on Investment).
     *  If this is the first transaction for the coin, it initializes the total amount and ROI accordingly.
     *  Otherwise, it updates the total amount and ROI based on the type of transaction (buy or sell).
     * @param transaction The transaction object to be updated
     * @param lastCoinTransaction An optional containing the last transaction of the same coin
     * @param dto The data transfer object containing the details of the new transaction
     */
    private void setTransactionValues(Transaction transaction, Optional<Transaction> lastCoinTransaction, AddTransactionDto dto){
        TransactionType transactionType = dto.getType();
        BigDecimal quantity = dto.getQuantity();
        BigDecimal price = dto.getPrice();
        String coinSymbol = dto.getSymbol();
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTransactionType(transactionType);
        Coin coin = coinRepository.findCoinByName(coinSymbol).orElseGet(() -> coinRepository.save(new Coin(coinSymbol)));
        transaction.setCoin(coin);

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
    }
}
