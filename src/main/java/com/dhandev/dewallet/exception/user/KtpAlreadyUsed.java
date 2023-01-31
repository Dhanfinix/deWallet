package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class KtpAlreadyUsed extends RuntimeException{
    public KtpAlreadyUsed(){
        super("Nomor KTP sudah digunakan");
    }
}
