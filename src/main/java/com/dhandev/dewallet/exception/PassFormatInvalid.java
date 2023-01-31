package com.dhandev.dewallet.exception;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class PassFormatInvalid extends RuntimeException{
    public PassFormatInvalid(){
        super("Password minimal terdiri dari 10 karakter dengan angka, huruf kapital dan kecil, serta simbol");
    }
}
