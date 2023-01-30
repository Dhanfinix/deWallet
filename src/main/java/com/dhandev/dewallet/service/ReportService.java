package com.dhandev.dewallet.service;

import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.TransactionRepository;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    //Get report from transaction table and check it with user table
    public List<GetReportDTO> getReport(LocalDate createdDate) throws Exception {
        List<TransactionModel> result = transactionRepository.findAllByTrxDate(createdDate);
        Iterable<UserModel> noReport = userRepository.findAll();
        ArrayList<GetReportDTO> response = new ArrayList<>();

        if (result.isEmpty()){
            throw new Exception("Report tidak ditemukan");
        } else{
            Map<String, List<TransactionModel>> collect = result.stream().collect(Collectors.groupingBy(TransactionModel::getUsername));
            Object[] key = collect.keySet().toArray();
            List<Object> listKey = Arrays.asList(key);
            System.out.println(collect.keySet());

            List<GetReportDTO> reportDTOS = new ArrayList<>();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
            //With forEach
            collect.forEach((user, transaksi) -> {
                GetReportDTO getReportDTO = new GetReportDTO();
                Double first = transaksi.get(0).getBalanceBefore().doubleValue();
                Double last = transaksi.get(transaksi.size() - 1).getBalanceAfter().doubleValue();

                Double changeBalance = last-first;
                getReportDTO.setChangeInPercentage(String.format("%,.2f", changeBalance/first*100) + "%");
                getReportDTO.setUsername(user);
                
                getReportDTO.setBalanceChangeDate(createdDate.format(dateFormat));
                reportDTOS.add(getReportDTO);
            });

            System.out.println("\t this is the key string "+ Arrays.toString(key));
            //Add user with no transaction on given date
            noReport.forEach((user) -> {
                //check every username that hasn't included in findByTrxdate
                if (!listKey.contains(user.getUsername())){
                    GetReportDTO getNoReport = new GetReportDTO();
                    getNoReport.setUsername(user.getUsername());
                    getNoReport.setChangeInPercentage("0%");
                    getNoReport.setBalanceChangeDate(createdDate.format(dateFormat));
                    reportDTOS.add(getNoReport);
                }
            });

            //With manually per key and per value
//            for (Object o : key) {                         //for loop key/ user
//                GetReportDTO getReportDTO = new GetReportDTO();
//                ArrayList<BigDecimal> balanceAfter = new ArrayList<>();
//                ArrayList<BigDecimal> balanceBefore = new ArrayList<>();
//                for (int v = 0; v < result.toArray().length; v++) {        //for loop value/ transaksi
//                    if (result.get(v).getUsername().equals(o)) {    //jika username pada value sama dengan key
//                        balanceAfter.add(result.get(v).getBalanceAfter());
//                        balanceBefore.add(result.get(v).getBalanceBefore());
//                        getReportDTO.setUsername(o.toString());
//                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
//                        getReportDTO.setBalanceChangeDate(LocalDate.parse(createdDate).format(dateFormat));
//                    }
//                }
//                System.out.println(balanceAfter);
//                Double firstBalance = balanceBefore.get(0).doubleValue();    //final balance of d-1
//                Double lastBalance = balanceAfter.get(balanceAfter.size() - 1).doubleValue();    //final balance from last transaction of today
//                Double changeBalance = lastBalance - firstBalance;
//                getReportDTO.setChangeInPercentage(String.format("%,.2f", changeBalance/firstBalance*100) + "%");
//                response.add(getReportDTO);
//            }

            return reportDTOS;
        }
    }

    //Only get report from user table
    public List<GetReportDTO> getTransactionByUser(LocalDate date){
        Iterable<UserModel> userModel = userRepository.findAll();
        List<GetReportDTO> reportDTOS = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");

        userModel.forEach(user -> {
            double first;
            double last;
            double changeBalance;
            GetReportDTO getReportDTO = new GetReportDTO();
            List<TransactionModel> trxByDate = user.getTransaction().stream()
                    .filter(t -> t.getTrxDate().equals(date))
                    .collect(Collectors.toList());
            if (!trxByDate.isEmpty()){
                first = trxByDate.get(0).getBalanceBefore().doubleValue();
                last = trxByDate.get(trxByDate.size() - 1).getBalanceAfter().doubleValue();
                changeBalance = last-first;
                getReportDTO.setChangeInPercentage(String.format("%,.2f", changeBalance/first*100) + "%");
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
