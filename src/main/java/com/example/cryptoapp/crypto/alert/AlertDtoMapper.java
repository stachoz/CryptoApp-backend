package com.example.cryptoapp.crypto.alert;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import com.example.cryptoapp.user.User;
import org.springframework.stereotype.Service;

@Service
public class AlertDtoMapper {
    private final CoinRepository coinRepository;

    public AlertDtoMapper(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Alert map(AddAlertDto dto, User currentUser){
        Alert alert = new Alert();
        alert.setInitialPrice(dto.getInitialPrice());
        alert.setAlertPrice(dto.getAlertPrice());
        alert.setUser(currentUser);
        String coinSymbol = dto.getCoinSymbol();
        Coin coin = coinRepository.findCoinByName(coinSymbol).orElseGet(() -> coinRepository.save(new Coin(coinSymbol)));
        alert.setCoin(coin);
        return alert;
    }

    public AlertDto map(Alert alert){
        AlertDto alertDto = new AlertDto();
        alertDto.setId(alert.getId());
        alertDto.setInitialPrice(alert.getInitialPrice());
        alertDto.setAlertPrice(alert.getAlertPrice());
        alertDto.setCoinSymbol(alert.getCoin().getName());
        return alertDto;
    }
}
