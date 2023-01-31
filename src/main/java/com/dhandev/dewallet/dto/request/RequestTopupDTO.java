package com.dhandev.dewallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RequestTopupDTO {
    @NotBlank(message = "Username must be declared")
    public String username;
    @NotBlank(message = "Password must be declared")
    public String password;
    @NotNull(message = "Amount must be declared")
    public BigDecimal amount;
}
