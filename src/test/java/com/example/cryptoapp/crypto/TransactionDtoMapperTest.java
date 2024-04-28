package com.example.cryptoapp.crypto;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.crypto.coin.coin.CoinRepository;
import com.example.cryptoapp.crypto.coin.trasnaction.AddTransactionDto;
import com.example.cryptoapp.crypto.coin.trasnaction.Transaction;
import com.example.cryptoapp.crypto.coin.trasnaction.TransactionDtoMapper;
import com.example.cryptoapp.crypto.coin.trasnaction.TransactionType;
import com.example.cryptoapp.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDtoMapperTest {

    @Mock
    private CoinRepository coinRepository;
    @InjectMocks
    private TransactionDtoMapper transactionDtoMapper;
    private AddTransactionDto addDto1;
    private AddTransactionDto addDto2;
    private Transaction lastCoinTransaction1;
    private Transaction lastCoinTransaction2;
    private Coin coin;
    private AddTransactionDto sellDto1;



    @BeforeEach
    public void init() {
        coin = new Coin();

        lastCoinTransaction1 = new Transaction();
        lastCoinTransaction1.setTotalAmount(new BigDecimal("2"));
        lastCoinTransaction1.setRoi(new BigDecimal("-4"));

        lastCoinTransaction2 = new Transaction();
        lastCoinTransaction2.setTotalAmount(new BigDecimal("4"));
        lastCoinTransaction2.setRoi(new BigDecimal("-16"));

        addDto1 = new AddTransactionDto();
        addDto1.setQuantity(new BigDecimal("2"));
        addDto1.setPrice(new BigDecimal("2"));
        addDto1.setType(TransactionType.BUY);
        addDto1.setSymbol("BTC");

        addDto2 = new AddTransactionDto();
        addDto2.setQuantity(new BigDecimal("3"));
        addDto2.setPrice(new BigDecimal("10"));
        addDto2.setType(TransactionType.BUY);
        addDto2.setSymbol("BTC");

        sellDto1 = new AddTransactionDto();
        sellDto1 = new AddTransactionDto();
        sellDto1.setQuantity(new BigDecimal("2"));
        sellDto1.setPrice(new BigDecimal("2"));
        sellDto1.setType(TransactionType.SELL);
        sellDto1.setSymbol("BTC");
    }

    @Test
    public void addFirstBuyTransaction() {
        Optional<Transaction> lastCoinTransaction = Optional.empty();

        User currentUser = new User();
        when(coinRepository.findCoinByName(addDto1.getSymbol()))
                .thenReturn(Optional.of(coin));

        Transaction transaction = transactionDtoMapper.map(addDto1, lastCoinTransaction, currentUser);
        assertThat(transaction.getTotalAmount()).isEqualTo(addDto1.getQuantity());
        BigDecimal value = addDto1.getPrice().multiply(addDto1.getQuantity());
        assertThat(transaction.getRoi()).isEqualTo(value.negate());
    }

    @Test
    public void makeSecondBuyTransaction(){
        Optional<Transaction> lastTransaction = Optional.of(lastCoinTransaction1);

        when(coinRepository.findCoinByName(any()))
                .thenReturn(Optional.of(coin));

        Transaction transaction = transactionDtoMapper.map(addDto1, lastTransaction, new User());
        assertThat(transaction.getRoi()).isEqualTo(new BigDecimal("-8"));
        assertThat(transaction.getTotalAmount()).isEqualTo(new BigDecimal("4"));
    }

    @Test
    public void sellAll(){
        Optional<Transaction> lastTransaction = Optional.of(lastCoinTransaction1);

        when(coinRepository.findCoinByName(any()))
                .thenReturn(Optional.of(coin));
        Transaction transaction = transactionDtoMapper.map(sellDto1, lastTransaction, new User());
        assertThat(transaction.getRoi()).isEqualTo(new BigDecimal("0"));
        assertThat(transaction.getTotalAmount()).isEqualTo(new BigDecimal("0"));
    }

    @Test
    public void sellPart(){
        Optional<Transaction> lastTransaction = Optional.of(lastCoinTransaction2);

        when(coinRepository.findCoinByName(any()))
                .thenReturn(Optional.of(coin));
        Transaction transaction = transactionDtoMapper.map(sellDto1, lastTransaction, new User());
        assertThat(transaction.getRoi()).isEqualTo(new BigDecimal("-12"));
        assertThat(transaction.getTotalAmount()).isEqualTo(new BigDecimal("2"));

    }


}