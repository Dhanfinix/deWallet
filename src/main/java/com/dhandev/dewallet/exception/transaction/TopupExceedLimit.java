package com.dhandev.dewallet.exception.transaction;

import com.dhandev.dewallet.constant.Constant;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
public class TopupExceedLimit extends RuntimeException{
    public TopupExceedLimit(){
        super("Jumlah melebihi nilai topup maksimum sebesar: " + Constant.MAX_TOPUP);
    }
}
