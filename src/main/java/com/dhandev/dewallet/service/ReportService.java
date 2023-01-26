package com.dhandev.dewallet.service;

import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.repository.TransactionRepository;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<GetReportDTO> getReport(String createdDate) throws Exception {
        List<TransactionModel> result = transactionRepository.findAllByTrxDate(LocalDate.parse(createdDate));
        ArrayList<GetReportDTO> response = new ArrayList<>();

        if (result.isEmpty()){
            throw new Exception("Report tidak ditemukan");
        } else{
            Map<String, List<TransactionModel>> collect = result.stream().collect(Collectors.groupingBy(TransactionModel::getUsername));
            Object[] key = collect.keySet().toArray();

            System.out.println(collect.keySet());
            for (Object o : key) {                         //for loop key/ user
                GetReportDTO getReportDTO = new GetReportDTO();
                ArrayList<BigDecimal> balanceAfter = new ArrayList<>();
                ArrayList<BigDecimal> balanceBefore = new ArrayList<>();
                for (var v = 0; v < result.toArray().length; v++) {        //for loop value/ transaksi
                    if (result.get(v).getUsername().equals(o)) {    //jika username pada value sama dengan key
                        balanceAfter.add(result.get(v).getBalanceAfter());
                        balanceBefore.add(result.get(v).getBalanceBefore());
                        getReportDTO.setUsername(o.toString());
                        getReportDTO.setBalanceChangeDate(LocalDate.parse(createdDate));
                    }
                }
                System.out.println(balanceAfter);
                var firstBalance = balanceBefore.get(0).doubleValue();    //final balance of d-1
                var lastBalance = balanceAfter.get(balanceAfter.size() - 1).doubleValue();    //final balance from last transaction of today
                var changeBalance = lastBalance - firstBalance;
                getReportDTO.setChangeInPercentage(String.format("%,.2f", changeBalance/firstBalance*100) + "%");
                response.add(getReportDTO);
            }

            return response;
        }
    }
}
