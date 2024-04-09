package com.example.cryptoapp.crypto.wallet.dto;

import com.example.cryptoapp.crypto.wallet.TransactionType;
import com.example.cryptoapp.validators.Enum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {
    @NotBlank
    @Size(min = 2, max = 10)
    @JsonProperty("coin")
    private String symbol;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    private BigDecimal quantity;

    @JsonProperty("type")
    @NotNull
    @Enum(enumClass = TransactionType.class)
    private String transactionType;
}
