package com.example.cryptoapp.crypto.transaction;

import com.example.cryptoapp.crypto.transaction.coin.Coin;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType type;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAdded;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "coin_id",
            referencedColumnName = "id"
    )
    private Coin coin;

}
