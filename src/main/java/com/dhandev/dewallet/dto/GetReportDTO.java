package com.dhandev.dewallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReportDTO {
    private String username;
    private String changeInPercentage;
    private String balanceChangeDate;
}
