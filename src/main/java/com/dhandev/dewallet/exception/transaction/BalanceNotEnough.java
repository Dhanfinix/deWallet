package com.dhandev.dewallet.exception.transaction;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class BalanceNotEnough extends RuntimeException{
    public BalanceNotEnough(){
        super("Saldo kurang untuk melakukan transaksi");
    }
}
