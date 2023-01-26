package com.dhandev.dewallet.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDTO {
    private String username;

    private String password;

    private String oldPassword;
}
