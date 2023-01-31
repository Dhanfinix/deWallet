package com.dhandev.dewallet.exception;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class NewPassEqualOldPass extends RuntimeException {
    public NewPassEqualOldPass(){
        super("Password baru sama dengan password lama");
    }
}
