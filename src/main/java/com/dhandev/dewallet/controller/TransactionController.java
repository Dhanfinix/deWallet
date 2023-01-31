package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.dto.CreateDTO;
import com.dhandev.dewallet.dto.request.RequestCreateDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.response.ResponseHandler;
import com.dhandev.dewallet.service.TransactionService;
import com.dhandev.dewallet.service.UserService;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody RequestCreateDTO request, BindingResult bindingResult){
        try{
            CreateDTO result = transactionService.createDTO(bindingResult, request.getUsername(), request.getPassword(), request.getDestinationUsername(), request.getAmount());
            return ResponseHandler.generateResponse("Berhasil Transfer", HttpStatus.OK, result);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/topup")
    public ResponseEntity<Object> topup(@RequestBody RequestCreateDTO request){
        try {
            transactionService.topUp(request.getUsername(), request.getPassword(), request.getAmount());
            return ResponseHandler.generateResponse("Berhasil Topup", HttpStatus.OK, null);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


}
