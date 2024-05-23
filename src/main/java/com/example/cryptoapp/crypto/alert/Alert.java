package com.example.cryptoapp.crypto.alert;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(scale = 7, precision = 30)
    private BigDecimal initialPrice;

    @Column(scale = 7, precision = 30)
    private BigDecimal alertPrice;

    @Min(1)
    @Max(100)
    private int repeatTimes;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Coin coin;
}
