package com.dhandev.dewallet.exception.transaction;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class AmountZeroOrNegative extends RuntimeException{
    public AmountZeroOrNegative(){
        super("Transaksi gagal karena amount bernilai negatif atau nol");
    }
}
