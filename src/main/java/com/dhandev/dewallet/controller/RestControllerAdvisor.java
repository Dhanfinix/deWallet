package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.exception.ErrorMessage;
import com.dhandev.dewallet.exception.FormatInvalid;
import com.dhandev.dewallet.exception.transaction.*;
import com.dhandev.dewallet.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * @author Muhammad Ramdhan on 1/31/2023
 */
@RestControllerAdvice
public class RestControllerAdvisor {
    @ExceptionHandler({
            NoUserFoundException.class,
            AccountBlocked.class,
            WrongPassword.class,
            NewPassEqualOldPass.class,
            PassFormatInvalid.class,
            FormatInvalid.class,
            UsernameAlreadyUsed.class,
            KtpFormatInvalid.class,
            KtpAlreadyUsed.class,
            //transaction
            NoRecipientNameFound.class,
            AmountZeroOrNegative.class,
            BalanceNotEnough.class,
            CreateExceedLimit.class,
            BalanceExceed.class,
            UnderMinTrx.class,
            TopupExceedLimit.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage noUserFoundException(RuntimeException ex, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

}
