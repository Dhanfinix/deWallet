package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class KtpFormatInvalid extends RuntimeException{
    public KtpFormatInvalid(){
        super("Nomor KTP harus 16 digit dan tidak mengandung huruf");
    }
}
