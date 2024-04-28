package com.example.cryptoapp.crypto.coin.trasnaction;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AddTransactionDto {
    @NotNull
    @Size(min = 2, max = 10)
    @JsonProperty("symbol")
    private String symbol;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    private BigDecimal quantity;
    @NotNull
    private TransactionType type;
}
