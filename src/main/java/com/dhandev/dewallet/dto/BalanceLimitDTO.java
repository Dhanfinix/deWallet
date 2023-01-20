package com.dhandev.dewallet.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceLimitDTO {
    private String balance;

    private String transactionLimit;
}
