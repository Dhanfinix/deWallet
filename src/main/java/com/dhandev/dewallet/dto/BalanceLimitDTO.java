package com.dhandev.dewallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceLimitDTO {
    private String balance;

    private String transactionLimit;
}
