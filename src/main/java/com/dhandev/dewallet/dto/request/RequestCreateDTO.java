package com.dhandev.dewallet.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RequestCreateDTO {
    public String username;
    public String password;
    public String destinationUsername;
    public BigDecimal amount;
}
