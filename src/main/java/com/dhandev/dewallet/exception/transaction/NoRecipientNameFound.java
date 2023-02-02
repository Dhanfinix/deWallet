package com.dhandev.dewallet.exception.transaction;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class NoRecipientNameFound extends RuntimeException{
    public NoRecipientNameFound(){
        super("Tidak ditemukan username penerima");
    }
}
