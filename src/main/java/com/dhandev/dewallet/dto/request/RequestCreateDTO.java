package com.dhandev.dewallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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


    @NotBlank(message = "Destionation Username must be declared")
    public String destinationUsername;

    @NotNull(message = "Amount must be declared")
    public BigDecimal amount;
}
