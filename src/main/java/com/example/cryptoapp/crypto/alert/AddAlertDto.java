package com.example.cryptoapp.crypto.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddAlertDto {
    @Min(0)
    private BigDecimal initialPrice;
    @NotNull
    @Min(0)
    private BigDecimal alertPrice;
    @NotNull
    @Min(1)
    @Max(100)
    private int repeatTimes;
    @JsonProperty("symbol")
    @NotNull
    private String coinSymbol;
}
