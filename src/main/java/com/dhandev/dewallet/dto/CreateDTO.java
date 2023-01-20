package com.dhandev.dewallet.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CreateDTO {
    private int trxId;
    private String originUsername;
    private String destinationUsername;
    private BigDecimal amount;
    private String status;
}
