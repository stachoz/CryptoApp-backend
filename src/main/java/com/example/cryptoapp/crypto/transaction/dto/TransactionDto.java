package com.example.cryptoapp.crypto.transaction.dto;

import com.example.cryptoapp.crypto.transaction.TransactionType;
import com.example.cryptoapp.validators.Enum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    @NotBlank
    @JsonProperty("coin")
    private String coinName;
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
