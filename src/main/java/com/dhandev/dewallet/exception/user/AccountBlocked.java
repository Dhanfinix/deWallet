package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class AccountBlocked extends RuntimeException{
    public AccountBlocked(){
        super("Akun anda terblokir");
    }
}
