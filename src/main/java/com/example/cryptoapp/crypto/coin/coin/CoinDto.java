package com.example.cryptoapp.crypto.coin.coin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinDto {
    @Size(min = 2, max = 10)
    private String symbol;
    @Min(0)
    @NotNull
    private BigDecimal quantity;
}
