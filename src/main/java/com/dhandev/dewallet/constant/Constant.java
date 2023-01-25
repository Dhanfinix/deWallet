package com.dhandev.dewallet.constant;

import java.math.BigDecimal;

public class Constant {

    public static final BigDecimal MAX_BALANCE = BigDecimal.valueOf(10_000_000);
    public static final BigDecimal MIN_BALANCE = BigDecimal.valueOf(10_000);
    public static final BigDecimal MAX_TRANSACTION = BigDecimal.valueOf(1_000_000);
    //LONG -MIN BALANCE
    //LONG -MAX TRANSACTION AMOUNT
    public static final BigDecimal MAX_TRANSACTION_WITH_KTP = BigDecimal.valueOf(5_000_000);
    //LONG -MAX TRANSACTION AMOUNT WITH KTP
    public static final BigDecimal MIN_TRANSACTION = BigDecimal.valueOf(10_000);
    //LONG -MIN TRANSACTION AMOUNT
    public static final BigDecimal MAX_TOPUP = BigDecimal.valueOf(10_000_000);
    //LONG- -MAX TOP UP
    public static final double TRANSACTION_TAX = 0.125;
    //DOUBLE -TRANSACTION TAX

    public static final String SETTLED = "SETTLED";

    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{10,}$";

    public static final String REGEX_KTP = "^[0-9]{1,16}$";
}
