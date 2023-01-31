package com.dhandev.dewallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder    //agar tidak perlu buat object
public class GetInfoDTO {
    private String username;

    private String ktp;
}
