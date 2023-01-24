package com.dhandev.dewallet.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GetReportDTO {
    private String username;
    private Double changeInPercentage;
    private Date balanceChangeDate;
}
