package com.example.cryptoapp.crypto;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import com.example.cryptoapp.crypto.coin.trasnaction.*;
import com.example.cryptoapp.crypto.coin.user_coin.*;
import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final BinanceApiConnector binanceApiConnector;
    private final CoinRepository coinRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final TransactionDtoMapper transactionDtoMapper;
    private final UserCoinDtoInfoMapper userCoinDtoInfoMapper;

    public WalletService(BinanceApiConnector binanceApiConnector, CoinRepository coinRepository, UserService userService,
                         TransactionRepository transactionRepository, TransactionDtoMapper transactionDtoMapper, UserCoinDtoInfoMapper userCoinDtoInfoMapper) {
        this.binanceApiConnector = binanceApiConnector;
        this.coinRepository = coinRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.transactionDtoMapper = transactionDtoMapper;
        this.userCoinDtoInfoMapper = userCoinDtoInfoMapper;
    }

    public List<String> getCoins(){
        return coinRepository.findAll()
                .stream().map(Coin::getName)
                .collect(Collectors.toList());
    }

    public List<UserCoinDtoInfo> getCurrentUserCoins(){
        User user = userService.getCurrentUser();
        return transactionRepository.getUserLastDistinctCoinsTransaction(user.getId()).stream()
                .map(userCoinDtoInfoMapper::map)
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

    private void validateTransaction(AddTransactionDto dto,  Optional<Transaction> lastCoinTransaction){
        String coinSymbol = dto.getSymbol();
        validateCoinBinanceSupport(coinSymbol);
        validateTransactionType(dto, lastCoinTransaction);
    }

    /**
     * @throws BinanceClientException
     */
    private void validateCoinBinanceSupport(String symbol){
        if(!coinRepository.existsByName(symbol)){
            binanceApiConnector.averagePriceRequest(symbol);
        }
    }

    private void validateTransactionType(AddTransactionDto dto, Optional<Transaction> lastCoinTransaction){
        TransactionType transactionType = dto.getType();
        if(lastCoinTransaction.isPresent()){
            Transaction lastTransaction = lastCoinTransaction.get();
            if(transactionType == TransactionType.SELL && dto.getQuantity().compareTo(lastTransaction.getTotalAmount()) > 0)  throw new OperationConflictException("you can not sell more than you have");
        } else {
            if(transactionType == TransactionType.SELL) throw new OperationConflictException("you can not sell coin that you do not have");
        }
    }
}