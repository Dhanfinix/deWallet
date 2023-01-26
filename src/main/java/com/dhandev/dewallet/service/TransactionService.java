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
        TransactionModel transactionSender = new TransactionModel();
        TransactionModel transactionTax = new TransactionModel();
        TransactionModel transactionRecipient = new TransactionModel();
        if (sender == null){
            throw new Exception("Tidak ditemukan username pengirim");
        }
        int attempt = sender.getPasswordAttempt();

        if (!sender.getPassword().equals(password)){
            if (attempt==3){
                sender.setBanned(true);
                throw new Exception("Akun anda terblokir");
            }
            sender.setPasswordAttempt(attempt+1);
            throw new Exception("Password salah");
        } else if (sender.isBanned()){
            throw new Exception("Akun anda terblokir");
        }

        UserModel recipient = userRepository.findByUsername(destinationUsername);

        if (recipient == null){
            throw new Exception("Tidak ditemukan username penerima");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) <0){
            throw new Exception("Top up gagal karena bernilai negatif");
        } else if (sender.getTransactionLimit().compareTo(amount) < 0){     //Jika limit lebih besar dari amount, lempar eror
            BigDecimal limit = sender.getTransactionLimit();
            currencyFormat(limit, new Locale("in", "ID"));
            throw new Exception("Jumlah melewati limit saat ini yaitu: " + limit);
        } else if (sender.getBalance().add(Constant.MIN_BALANCE).compareTo(amount) < 0){          //balance kurang
            throw new Exception("Saldo kurang untuk melakukan transaksi");
        } else if (Constant.MIN_TRANSACTION.compareTo(amount) > 0){         //transaksi harus lebih besar daripada batas minimal
            BigDecimal min_trx = Constant.MIN_TRANSACTION;
            throw new Exception("Minimum transaksi adalah: " + min_trx);
        }

        sender.setPasswordAttempt(0);
        BigDecimal tax = amount.multiply(BigDecimal.valueOf(Constant.TRANSACTION_TAX));        //jumlah tax
        BigDecimal newAmount = amount.add(tax);   //amount setelah potong tax

        //sender
        transactionSender.setUsername(username);
        transactionSender.setAmount(amount);
        transactionSender.setStatus(Constant.SETTLED);
        transactionSender.setTrxDate(LocalDate.now());
        transactionSender.setBalanceBefore(sender.getBalance());
        transactionSender.setType(Constant.TYPE_SENDER);

        sender.setBalance(sender.getBalance().subtract(amount));       //mengurangi saldo sender dari amount untuk userdatabase

        //sender tax, catat transaksi khusus tax
        transactionTax.setUsername(username);
        transactionTax.setAmount(tax);
        transactionTax.setStatus(Constant.SETTLED);
        transactionTax.setTrxDate(LocalDate.now());
        transactionTax.setType(Constant.TYPE_SENDER_TAX);
        transactionTax.setBalanceBefore(sender.getBalance());
        transactionTax.setBalanceAfter(sender.getBalance().subtract(tax));

        transactionSender.setBalanceAfter(sender.getBalance());


        //recipient
        transactionRecipient.setUsername(destinationUsername);
        transactionRecipient.setAmount(amount);
        transactionRecipient.setStatus(Constant.SETTLED);
        transactionRecipient.setTrxDate(LocalDate.now());
        transactionRecipient.setBalanceBefore(recipient.getBalance());
        transactionRecipient.setType(Constant.TYPE_RECIPIENT);

        recipient.setBalance(recipient.getBalance().add(amount));       //menambah saldo recipient
        transactionRecipient.setBalanceAfter(recipient.getBalance());

        transactionSender.setUserModel(sender);
        transactionTax.setUserModel(sender);
        transactionRecipient.setUserModel(recipient);
        transactionRepository.saveAll(List.of(transactionSender, transactionTax, transactionRecipient));

        //COBA HAPUS, KAN UDAH SET USER DI TRANSACTION, yup bisa dihapus
//        userRepository.save(sender);
//        userRepository.save(recipient);
        return CreateDTO.builder()
                .trxId(transactionSender.getTrxId())
                .originUsername(sender.getUsername())
                .destinationUsername(recipient.getUsername())
                .amount(currencyFormat(amount, new Locale("in", "ID")))
                .status(Constant.SETTLED)
                .build();
    }

    public void topUp(String username, String password, BigDecimal amount) throws Exception{
        UserModel userModel = userRepository.findByUsername(username);
        TransactionModel transaction = new TransactionModel();
        BigDecimal maxTopUp = Constant.MAX_TOPUP;
        if (userModel == null) {
            throw new Exception("Username tidak ditemukan");
        }
        int attempt = userModel.getPasswordAttempt();
        if (!userModel.getPassword().equals(password)){
            if (attempt==3){
                userModel.setBanned(true);
                throw new Exception("Password salah tiga kali, akun terkunci");
            }
            userModel.setPasswordAttempt(attempt+1);
            throw new Exception("Password salah");
        } else if (userModel.isBanned()){
            throw new Exception("Akun anda terblokir");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new Exception("Top up gagal karena bernilai negatif atau nol");
        } else if (maxTopUp.compareTo(amount) < 0){
            throw new Exception("Jumlah melebihi nilai topup maksimum sebesar: " + maxTopUp);
        } else if (userModel.getBalance().add(amount).compareTo(Constant.MAX_BALANCE) > 0){
            throw new Exception("Saldo maksimal terlampaui");
        }
        userModel.setPasswordAttempt(0);

        transaction.setUsername(username);
        transaction.setAmount(amount);
        transaction.setStatus(Constant.SETTLED);
        transaction.setUserModel(userModel);
        transaction.setTrxDate(LocalDate.now());
        transaction.setBalanceBefore(userModel.getBalance());
        transaction.setType(Constant.TYPE_TOPUP);

        userModel.setBalance(userModel.getBalance().add(amount));
        transaction.setBalanceAfter(userModel.getBalance());

        transactionRepository.save(transaction);
    }


}
