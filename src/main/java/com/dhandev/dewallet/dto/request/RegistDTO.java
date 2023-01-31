package com.dhandev.dewallet.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
