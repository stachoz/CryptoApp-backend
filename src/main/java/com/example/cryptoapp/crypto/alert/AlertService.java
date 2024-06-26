package com.example.cryptoapp.crypto.alert;

import com.binance.connector.client.exceptions.BinanceClientException;
import com.example.cryptoapp.crypto.BinanceApiConnector;
import com.example.cryptoapp.crypto.BinanceValidator;
import com.example.cryptoapp.exception.OperationConflictException;
import com.example.cryptoapp.mail.MailService;
import com.example.cryptoapp.user.User;
import com.example.cryptoapp.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.mail.MailSendException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertDtoMapper alertDtoMapper;
    private final BinanceValidator binanceValidator;
    private final UserService userService;
    private final BinanceApiConnector binanceApiConnector;
    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public AlertService(AlertRepository alertRepository, AlertDtoMapper alertDtoMapper, BinanceValidator binanceValidator, UserService userService, BinanceApiConnector binanceApiConnector, ObjectMapper objectMapper, MailService mailService) {
        this.alertRepository = alertRepository;
        this.alertDtoMapper = alertDtoMapper;
        this.binanceValidator = binanceValidator;
        this.userService = userService;
        this.binanceApiConnector = binanceApiConnector;
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    public AlertDto addAlert(AddAlertDto dto){
        String coinSymbol = dto.getCoinSymbol();
        if(dto.getInitialPrice() == null){
            dto.setInitialPrice(getCoinAveragePrice(coinSymbol));
        } else {
            binanceValidator.validateCoinBinanceSupport(coinSymbol);
        }
        User currentUser = userService.getCurrentUser();
        if(alertRepository.existsByAlertPriceAndUserIdAndCoin_Name(dto.getAlertPrice(), currentUser.getId(), coinSymbol)) {
           throw new OperationConflictException("Alert for coin with this price already exists");
        }
        Alert alert = alertDtoMapper.map(dto, currentUser);
        Alert savedAlert = alertRepository.save(alert);
        return alertDtoMapper.map(savedAlert);
    }

    public List<AlertDto> getUserAlerts(){
        User currentUser = userService.getCurrentUser();
        return alertRepository.findAllByUserId(currentUser.getId())
                .stream().map(alertDtoMapper::map)
                .collect(Collectors.toList());
    }

    public void deleteUserAlertById(Long alertId){
        User user = userService.getCurrentUser();
        Alert toDelete = alertRepository.findById(alertId).orElseThrow(() -> new NoSuchElementException("Alert with this id does not exists"));
        if(toDelete.getUser().getId().equals(user.getId())){
            alertRepository.delete(toDelete);
        }
    }

    public void sendAlertById(Long id){
        try {
            Alert alert = findAndDeleteAlertById(id);
            BigDecimal initialPrice = alert.getInitialPrice();
            BigDecimal alertPrice = alert.getAlertPrice();
            String coinName = alert.getCoin().getName();
            String mailSubject = coinName + " price is " + alertPrice + "$!";
            String mailBody = coinName + " price has " + specifyPriceDirection(initialPrice, alertPrice) +
                    " from " + initialPrice + "$ to " + alertPrice + "$.";
            System.out.println(mailBody);
            mailService.sendEmail(alert.getUser().getEmail(), mailSubject, mailBody);
        } catch (StaleObjectStateException e){
            System.out.println(e.getMessage());
        } catch (MailSendException e){
            System.out.println(e.getMessage());
        } catch (PessimisticLockingFailureException e) {
            System.out.println("Failed to acquire lock: " + e.getMessage());
        } catch (ObjectOptimisticLockingFailureException e){
            System.out.println("Optimistic locking: " + e.getMessage());
        }
    }

    private Alert findAndDeleteAlertById(Long id){
        Alert alert = alertRepository.findByIdWithLock(id).orElseThrow(() -> new NoSuchElementException("alert with id (" + id + ") does not exists"));
        alertRepository.deleteById(id);
        return alert;
    }

    /**
     * If the coin symbol is not supported by Binance there will be thrown an exception. This exception
     * is handled by GlobalExceptionHandler
     * @param symbol - coin symbol
     * @throws BinanceClientException
     */
    private BigDecimal getCoinAveragePrice(String symbol){
        String response = binanceApiConnector.averagePriceRequest(symbol);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String price = jsonNode.get("price").asText();
            return new BigDecimal(price);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String specifyPriceDirection(BigDecimal initialPrice, BigDecimal alertPrice){
        return initialPrice.compareTo(alertPrice) > 0 ? "decreased" : "increased";
    }
}
