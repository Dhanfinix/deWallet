package com.dhandev.dewallet.exception.transaction;

import com.dhandev.dewallet.exception.user.NoUserFoundException;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class NoRecipientNameFound extends RuntimeException{
    public NoRecipientNameFound(){
        super("Tidak ditemukan username penerima");
    }
}
