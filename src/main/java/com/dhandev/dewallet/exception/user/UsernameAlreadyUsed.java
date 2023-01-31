package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class UsernameAlreadyUsed extends RuntimeException{
    public UsernameAlreadyUsed(){
        super("Username sudah digunakan");
    }
}
