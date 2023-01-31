package com.dhandev.dewallet.exception;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class AccountBlocked extends RuntimeException{
    public AccountBlocked(){
        super("Akun anda terblokir");
    }
}
