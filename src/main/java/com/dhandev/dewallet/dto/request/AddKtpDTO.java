package com.dhandev.dewallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddKtpDTO {

    private int id;
    @NotBlank
    private String ktp;

}
