package com.example.cryptoapp.crypto;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import org.springframework.stereotype.Service;

@Service
public class BinanceValidator {
    private final CoinRepository coinRepository;
    private final BinanceApiConnector binanceApiConnector;

    public BinanceValidator(CoinRepository coinRepository, BinanceApiConnector binanceApiConnector) {
        this.coinRepository = coinRepository;
        this.binanceApiConnector = binanceApiConnector;
    }

    /**
     * @throws BinanceClientException
     * exception in caught inside GlobalExceptionHandler
     */
    public void validateCoinBinanceSupport(String symbol){
        if(!coinRepository.existsByName(symbol)){
            System.out.println(binanceApiConnector.averagePriceRequest(symbol));
        }
    }
}
