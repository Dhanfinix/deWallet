package com.dhandev.dewallet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder    //agar tidak perlu buat object
public class UserDTO {
    private String username;

    private String ktp;
}
