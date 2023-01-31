package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class WrongPassword extends RuntimeException{
    public WrongPassword(){
        super("Password salah");
    }
}
