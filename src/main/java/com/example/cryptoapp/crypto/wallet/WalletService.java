package com.example.cryptoapp.crypto.wallet;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.example.cryptoapp.crypto.BinanceApiConnector;
import com.example.cryptoapp.crypto.wallet.coin.*;
import com.example.cryptoapp.crypto.wallet.coin.user_coin.UserCoin;
import com.example.cryptoapp.crypto.wallet.coin.user_coin.UserCoinId;
import com.example.cryptoapp.crypto.wallet.coin.user_coin.UserCoinRepository;
import com.example.cryptoapp.crypto.wallet.dto.TransactionDto;
import com.example.cryptoapp.crypto.wallet.dto.TransactionDtoMapper;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class WalletService {
    private final TransactionDtoMapper transactionDtoMapper;
    private final TransactionRepository transactionRepository;
    private final BinanceApiConnector binanceApiConnector;
    private final CoinRepository coinRepository;
    private final UserService userService;
    private final CoinDtoMapper coinDtoMapper;
    private final UserCoinRepository userCoinRepository;

    public WalletService(TransactionDtoMapper transactionDtoMapper, TransactionRepository transactionRepository, BinanceApiConnector binanceApiConnector, CoinRepository coinRepository, UserService userService, CoinDtoMapper coinDtoMapper,
                         UserCoinRepository userCoinRepository) {
        this.transactionDtoMapper = transactionDtoMapper;
        this.transactionRepository = transactionRepository;
        this.binanceApiConnector = binanceApiConnector;
        this.coinRepository = coinRepository;
        this.userService = userService;
        this.coinDtoMapper = coinDtoMapper;
        this.userCoinRepository = userCoinRepository;
    }

    public Transaction addTransaction(TransactionDto dto){
        coinValidation(dto.getSymbol());
        Transaction transaction = transactionDtoMapper.map(dto);
        return transactionRepository.save(transaction);
    }

    public void addCoin(CoinDto coinDto){
        coinValidation(coinDto.getSymbol());
        UserCoin userCoin = coinDtoMapper.map(coinDto);
        Coin coin = coinRepository.findCoinByName(coinDto.getSymbol()).orElseThrow(() -> new NoSuchElementException("Coin does not exists"));
        User user = userService.getCurrentUser();
        setUserCoinId(userCoin, user.getId(), coin.getId());
        userCoin.setUser(user);
        userCoin.setCoin(coin);
        userCoinRepository.save(userCoin);
    }

    /**
     * @throws BinanceClientException
     */
    private void coinValidation(String symbol){
        if(!coinRepository.existsByName(symbol)){
            binanceApiConnector.averagePriceRequest(symbol);
        }
    }

    private void setUserCoinId(UserCoin userCoin, Long userId, Long coinId){
        UserCoinId userCoinId = new UserCoinId(userId, coinId);
        userCoin.setId(userCoinId);
    }
}