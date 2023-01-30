package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.response.ResponseHandler;
import com.dhandev.dewallet.service.ReportService;
import com.dhandev.dewallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/getReportOld/{date}")
    public ResponseEntity<Object> getReportOld(@PathVariable LocalDate date){
        try{
            List<GetReportDTO> result = reportService.getReport(date);
            return ResponseHandler.generateResponse("Berikut report pada tanggal yang diminta", HttpStatus.OK, result);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/getReport/{date}")
    public ResponseEntity<Object> getReport(@PathVariable LocalDate date){
        try {
            List<GetReportDTO> result = reportService.getTransactionByUser(date);
            return ResponseHandler.generateResponse("Berikut report pada tanggal yang diminta", HttpStatus.OK, result);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
