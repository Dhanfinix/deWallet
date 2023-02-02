package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.dto.CreateDTO;
import com.dhandev.dewallet.dto.request.RequestCreateDTO;
import com.dhandev.dewallet.dto.request.RequestTopupDTO;
import com.dhandev.dewallet.exception.FormatInvalid;
import com.dhandev.dewallet.response.ResponseHandler;
import com.dhandev.dewallet.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody RequestCreateDTO request, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FormatInvalid();
        }
        CreateDTO result = transactionService.createDTO(request.getUsername(), request.getPassword(), request.getDestinationUsername(), request.getAmount());
        return ResponseHandler.generateResponse("Berhasil Transfer", HttpStatus.OK, result);
    }

    @PostMapping("/topup")
    public ResponseEntity<Object> topup(@Valid @RequestBody RequestTopupDTO request, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FormatInvalid();
        }
        transactionService.topUp(request.getUsername(), request.getPassword(), request.getAmount());
        return ResponseHandler.generateResponse("Berhasil Topup", HttpStatus.OK, null);
    }


}
