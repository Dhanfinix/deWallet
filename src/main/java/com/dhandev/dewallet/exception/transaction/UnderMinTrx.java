package com.dhandev.dewallet.exception.transaction;

import java.math.BigDecimal;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class UnderMinTrx extends RuntimeException{
    public UnderMinTrx(BigDecimal minTrx){
        super("Minimum transaksi adalah: " + minTrx);
    }
}
