package com.dhandev.dewallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateDTO {
    private int trxId;
    private String originUsername;
    private String destinationUsername;
    private String amount;
    private String status;
}
