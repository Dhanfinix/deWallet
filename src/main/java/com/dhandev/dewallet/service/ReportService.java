package com.dhandev.dewallet.service;

import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.TransactionRepository;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // get report only from user table
    public List<GetReportDTO> getReport(LocalDate date){
        Iterable<UserModel> userModel = userRepository.findAll();
        List<GetReportDTO> reportDTOS = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");

        userModel.forEach(user -> {
            GetReportDTO getReportDTO = new GetReportDTO();
            List<TransactionModel> trxByDate = user.getTransaction().stream()
                    .filter(t -> t.getTrxDate().equals(date))
                    .collect(Collectors.toList());
            if (!trxByDate.isEmpty()){
                Double first = trxByDate.get(0).getBalanceBefore().doubleValue();
                Double last = trxByDate.get(trxByDate.size() - 1).getBalanceAfter().doubleValue();
                Double changeBalance = last-first;
                Double percentage = changeBalance/first*100;
                if (percentage.isInfinite()){
                    getReportDTO.setChangeInPercentage("-");
                } else {
                    getReportDTO.setChangeInPercentage(String.format("%,.2f", percentage) + "%");
                }
            } else {
                getReportDTO.setChangeInPercentage("0%");
            }
            getReportDTO.setUsername(user.getUsername());
            getReportDTO.setBalanceChangeDate(date.format(dateFormat));
            reportDTOS.add(getReportDTO);
        });

        return reportDTOS;
    }
}
