package com.dhandev.dewallet.exception.user;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class NoUserFoundException extends RuntimeException{
    public NoUserFoundException() {
        super("Username tidak ditemukan");
    }
}
