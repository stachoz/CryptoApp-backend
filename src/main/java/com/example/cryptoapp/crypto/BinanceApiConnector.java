package com.example.cryptoapp.crypto;

import com.binance.connector.client.impl.SpotClientImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BinanceApiConnector {
    private static final String FIAT = "USDT";
    public String averagePriceRequest(String symbol) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        SpotClientImpl client = new SpotClientImpl();
        parameters.put("symbol", symbol.toUpperCase() + FIAT);
        return client.createMarket().averagePrice(parameters);
    }
}
