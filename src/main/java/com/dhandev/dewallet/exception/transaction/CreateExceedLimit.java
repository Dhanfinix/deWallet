package com.dhandev.dewallet.exception.transaction;

import java.math.BigDecimal;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class CreateExceedLimit extends RuntimeException{
    public CreateExceedLimit(BigDecimal limit){
        super("Jumlah melewati limit pengiriman saat ini yaitu: " + limit);
    }
}
