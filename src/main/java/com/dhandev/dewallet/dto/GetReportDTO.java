package com.dhandev.dewallet.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class GetReportDTO {
    private String username;
    private String changeInPercentage;
    private LocalDate balanceChangeDate;
}
