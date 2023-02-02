package com.dhandev.dewallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String oldPassword;
}
