package com.dhandev.dewallet.exception.transaction;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class BalanceExceed extends RuntimeException{
    public BalanceExceed(){
        super("Saldo maksimal terlampaui");
    }
}
