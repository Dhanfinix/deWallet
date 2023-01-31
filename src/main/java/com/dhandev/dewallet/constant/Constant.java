package com.dhandev.dewallet.constant;

import java.math.BigDecimal;

public class Constant {

    public static final BigDecimal MAX_BALANCE = BigDecimal.valueOf(10_000_000);
    public static final BigDecimal MIN_BALANCE = BigDecimal.valueOf(10_000);
    public static final BigDecimal MAX_TRANSACTION = BigDecimal.valueOf(1_000_000);
    public static final BigDecimal MAX_TRANSACTION_WITH_KTP = BigDecimal.valueOf(5_000_000);
    public static final BigDecimal MIN_TRANSACTION = BigDecimal.valueOf(10_000);
    public static final BigDecimal MAX_TOPUP = BigDecimal.valueOf(10_000_000);
    public static final double TRANSACTION_TAX = 0.125;
    public static final String SETTLED = "SETTLED";
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{10,}$";
    public static final String REGEX_KTP = "^[0-9]{16}$";     //only number, length min 1 max 16
    public static final String TYPE_SENDER = "SENDER";
    public static final String TYPE_SENDER_TAX = "TAX";
    public static final String TYPE_RECIPIENT = "RECIPIENT";
    public static final String TYPE_TOPUP = "TOPUP";
}
