package com.dhandev.dewallet.service;

import com.dhandev.dewallet.constant.Constant;
import com.dhandev.dewallet.dto.CreateDTO;
import com.dhandev.dewallet.dto.GetReportDTO;
import com.dhandev.dewallet.model.TransactionModel;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.TransactionRepository;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.dhandev.dewallet.service.UserService.currencyFormat;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CreateDTO createDTO(String username, String password, String destinationUsername, BigDecimal amount) throws Exception{
        UserModel sender = userRepository.findByUsername(username);
        UserModel recipient = userRepository.findByUsername(destinationUsername);
        var attempt = sender.getPasswordAttempt();
        TransactionModel transactionSender = new TransactionModel();
        TransactionModel transactionRecipient = new TransactionModel();
        if (sender.getUsername().isEmpty()){
            throw new Exception("Tidak ditemukan username pengirim");
        } else if (!sender.getPassword().equals(password)){
            if (attempt>3){
                sender.setBanned(true);
                throw new Exception("Akun anda terblokir");
            }
            sender.setPasswordAttempt(attempt+1);
            throw new Exception("Password salah");
        } else if (sender.isBanned()){
            throw new Exception("Akun anda terblokir");
        } else if (recipient.getUsername().isEmpty()){
            throw new Exception("Tidak ditemukan username penerima");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) <0){
            throw new Exception("Top up gagal karena bernilai negatif");
        } else if (sender.getTransactionLimit().compareTo(amount) < 0){     //Jika limit lebih besar dari amount, lempar eror
            var limit = sender.getTransactionLimit();
            currencyFormat(limit, new Locale("in", "ID"));
            throw new Exception("Jumlah melewati limit saat ini yaitu: " + limit);
        } else if (sender.getBalance().add(Constant.MIN_BALANCE).compareTo(amount) < 0){          //balance kurang
            throw new Exception("Saldo kurang untuk melakukan transaksi");
        } else if (Constant.MIN_TRANSACTION.compareTo(amount) > 0){         //transaksi harus lebih besar daripada batas minimal
            var min_trx = Constant.MIN_TRANSACTION;
            throw new Exception("Minimum transaksi adalah: " + min_trx);
        }

        sender.setPasswordAttempt(0);
        var newAmount = amount.subtract(amount.multiply(BigDecimal.valueOf(Constant.TRANSACTION_TAX)));
        sender.setBalance(sender.getBalance().subtract(newAmount));       //mengurangi saldo sender
        recipient.setBalance(recipient.getBalance().add(newAmount));       //menambah saldo recipient

        transactionSender.setUsername(username);
        transactionSender.setAmount(newAmount.negate());
        transactionSender.setStatus(Constant.SETTLED);
        transactionSender.setTrxDate(LocalDate.now());
        transactionSender.setBalance(sender.getBalance());
        ////
        transactionRecipient.setUsername(destinationUsername);
        transactionRecipient.setAmount(newAmount);
        transactionRecipient.setStatus(Constant.SETTLED);
        transactionRecipient.setTrxDate(LocalDate.now());
        transactionRecipient.setBalance(recipient.getBalance());

        transactionSender.setUserModel(sender);
        transactionRecipient.setUserModel(recipient);
        transactionRepository.saveAll(List.of(transactionSender, transactionRecipient));

        //COBA HAPUS, KAN UDAH SET USER DI TRANSACTION, yup bisa dihapus
//        userRepository.save(sender);
//        userRepository.save(recipient);
        return CreateDTO.builder()
                .trxId(transactionSender.getTrxId())
                .originUsername(sender.getUsername())
                .destinationUsername(recipient.getUsername())
                .amount(newAmount.divide(BigDecimal.valueOf(1000), RoundingMode.DOWN))     //Pembulatan kebawah
                .status(Constant.SETTLED)
                .build();
    }

    public void topUp(String username, String password, BigDecimal amount) throws Exception{
        UserModel userModel = userRepository.findByUsername(username);
        TransactionModel transaction = new TransactionModel();
        var maxTopUp = Constant.MAX_TOPUP;
        if (userModel == null) {
            throw new Exception("Username tidak ditemukan");
        }
        var attempt = userModel.getPasswordAttempt();
        if (!userModel.getPassword().equals(password)){
            if (attempt>3){
                userModel.setBanned(true);
                throw new Exception("Password salah tiga kali, akun terkunci");
            }
            userModel.setPasswordAttempt(attempt+1);
            throw new Exception("Password salah");
        } else if (userModel.isBanned()){
            throw new Exception("Akun anda terblokir");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) <0){
            throw new Exception("Top up gagal karena bernilai negatif");
        } else if (maxTopUp.compareTo(amount) < 0){
            throw new Exception("Jumlah melebihi nilai topup maksimum sebesar: " + maxTopUp);
        } else if (userModel.getBalance().add(amount).compareTo(Constant.MAX_BALANCE) > 0){
            throw new Exception("Saldo maksimal terlampaui");
        }
        userModel.setPasswordAttempt(0);

        userModel.setBalance(userModel.getBalance().add(amount));
        transaction.setUsername(username);
        transaction.setAmount(amount);
        transaction.setStatus(Constant.SETTLED);
        transaction.setUserModel(userModel);
        transaction.setTrxDate(LocalDate.now());
        transaction.setBalance(userModel.getBalance());

        transactionRepository.save(transaction);
    }

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
                ArrayList<BigDecimal> balance = new ArrayList<>();
                for (var v = 0; v < result.toArray().length; v++) {        //for loop value/ transaksi
                    if (result.get(v).getUsername().equals(o)) {    //jika username pada value sama dengan key
                        balance.add(result.get(v).getBalance());
                        getReportDTO.setUsername(o.toString());
                        getReportDTO.setBalanceChangeDate(LocalDate.parse(createdDate));
                    }
                }
                System.out.println(balance);
                var firstBalance = balance.get(0);
                var lastBalance = balance.get(balance.size() - 1);
                var changeBalance = lastBalance.subtract(firstBalance);
                getReportDTO.setChangeInPercentage(changeBalance.divide(lastBalance, RoundingMode.DOWN) + "%");
                response.add(getReportDTO);
            }

            return response;
        }
    }
}
