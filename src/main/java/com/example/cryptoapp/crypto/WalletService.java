package com.example.cryptoapp.crypto;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import com.example.cryptoapp.crypto.coin.trasnaction.*;
import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final CoinRepository coinRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final TransactionDtoMapper transactionDtoMapper;
    private final BinanceValidator binanceValidator;

    public WalletService(CoinRepository coinRepository, UserService userService,
                         TransactionRepository transactionRepository, TransactionDtoMapper transactionDtoMapper, BinanceValidator binanceValidator) {
        this.coinRepository = coinRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.transactionDtoMapper = transactionDtoMapper;
        this.binanceValidator = binanceValidator;
    }

    public List<String> getCoins(){
        return coinRepository.findAll()
                .stream().map(Coin::getName)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getCurrentUserLastTransactionOnUniqueCoins(){
        User user = userService.getCurrentUser();
        return transactionRepository.getUserLastDistinctCoinsTransactions(user.getId()).stream()
                .map(transactionDtoMapper::map)
                .collect(Collectors.toList());
    }

    public TransactionDto addTransaction(AddTransactionDto dto){
        String symbol = dto.getSymbol();
        User user = userService.getCurrentUser();
        Optional<Transaction> lastCoinTransaction = transactionRepository.findFirstTransactionByUserIdAndAndCoin_NameOrderByTimeAddedDesc(user.getId(), symbol);
        validateTransaction(dto, lastCoinTransaction);
        Transaction saved = transactionRepository.save(transactionDtoMapper.map(dto, lastCoinTransaction, user));
        return transactionDtoMapper.map(saved);
    }

    public void deleteLastUserTransactionOnCoin(String coinSymbol){
        User user = userService.getCurrentUser();
        Transaction transactionToDelete = transactionRepository.findFirstTransactionByUserIdAndAndCoin_NameOrderByTimeAddedDesc(user.getId(), coinSymbol.toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("transaction on coin (" + coinSymbol + ") does not exists"));
        transactionRepository.delete(transactionToDelete);
    }

    public TransactionDto updateLastCoinTransaction(AddTransactionDto dto, String coinSymbolToUpdate){
        User user = userService.getCurrentUser();
        Long userId = user.getId();
        Transaction transactionToUpdate = transactionRepository.findFirstTransactionByUserIdAndAndCoin_NameOrderByTimeAddedDesc(userId, coinSymbolToUpdate)
                .orElseThrow(() -> new NoSuchElementException("transaction not found"));
        Optional<Transaction> previousTransaction = transactionRepository.findUserSecondTransactionOnCoin(userId, dto.getSymbol());
        validateTransaction(dto, previousTransaction);
        transactionDtoMapper.mapDtoToExistingTransaction(dto, transactionToUpdate, previousTransaction);
        Transaction saved = transactionRepository.save(transactionToUpdate);
        return transactionDtoMapper.map(saved);
    }

    private void validateTransaction(AddTransactionDto dto,  Optional<Transaction> lastCoinTransaction){
        String coinSymbol = dto.getSymbol();
        binanceValidator.validateCoinBinanceSupport(coinSymbol);
        validateTransactionType(dto, lastCoinTransaction);
    }


    private void validateTransactionType(AddTransactionDto dto, Optional<Transaction> lastCoinTransaction){
        if(lastCoinTransaction.isPresent()){
            if(dto.getType() == TransactionType.SELL && dto.getQuantity().compareTo(lastCoinTransaction.get().getTotalAmount()) > 0)
                throw new OperationConflictException("you can not sell more than you have");
        } else {
            if(dto.getType() == TransactionType.SELL) throw new OperationConflictException("you can not sell coin that you do not have");
        }
    }
}