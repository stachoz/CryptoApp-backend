package com.example.cryptoapp.crypto.coin.trasnaction;

import com.example.cryptoapp.crypto.coin.coin.Coin;
import com.example.cryptoapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(scale = 7, precision = 30)
    private BigDecimal quantity;

    @Column(scale = 7, precision = 30)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(scale = 7, precision = 30)
    private BigDecimal totalAmount;

    @Column(scale = 7, precision = 30)
    private BigDecimal roi;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAdded;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private User user;
}
