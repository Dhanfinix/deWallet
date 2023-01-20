package com.dhandev.dewallet.service;

import com.dhandev.dewallet.constant.Constant;
import com.dhandev.dewallet.dto.CreateDTO;
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
import java.util.Locale;

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
        TransactionModel transaction = new TransactionModel();
        if (sender.getUsername().isEmpty()){
            throw new Exception("Tidak ditemukan username pengirim");
        } else if (!sender.getPassword().equals(password)){
            throw new Exception("Password salah");
        } else if (recipient.getUsername().isEmpty()){
            throw new Exception("Tidak ditemukan username penerima");
        } else if (sender.getTransactionLimit().compareTo(amount) < 0){     //Jika limit lebih besar dari amount, lempar eror
            var limit = sender.getTransactionLimit();
            UserService.currencyFormat(limit, new Locale("in", "ID"));
            throw new Exception("Jumlah melewati limit saat ini yaitu: " + limit);
        } else if (sender.getBalance().add(Constant.MIN_BALANCE).compareTo(amount) < 0){          //balance kurang
            throw new Exception("Saldo kurang untuk melakukan transaksi");
        } else if (Constant.MIN_TRANSACTION.compareTo(amount) > 0){         //transaksi harus lebih besar daripada batas minimal
            var min_trx = Constant.MIN_TRANSACTION;
            throw new Exception("Minimum transaksi adalah: " + min_trx);
        }
            else {
            sender.setBalance(sender.getBalance().subtract(amount));       //mengurangi saldo sender
            recipient.setBalance(recipient.getBalance().add(amount));       //menambah saldo recipient

            transaction.setOriginUsername(username);
            transaction.setDestinationUsername(destinationUsername);
            transaction.setAmount(amount);
            transaction.setStatus(Constant.SETTLED);

            userRepository.save(sender);
            userRepository.save(recipient);
            transactionRepository.save(transaction);
        }
        return CreateDTO.builder()
                .trxId(transaction.getTrxId())
                .originUsername(sender.getUsername())
                .destinationUsername(recipient.getUsername())
                .amount(amount)
                .status(Constant.SETTLED)
                .build();
    }
}
