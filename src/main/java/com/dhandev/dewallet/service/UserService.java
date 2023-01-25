package com.dhandev.dewallet.service;

import com.dhandev.dewallet.constant.Constant;
import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.UserDTO;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public UserModel register(String name, String password) throws Exception {
        UserModel userModel = new UserModel();
        if (!name.isEmpty() && userRepository.findByUsernameEquals(name).isEmpty()){
            userModel.setUsername(name);
            userModel.setBalance(Constant.MIN_BALANCE);            //TODO: DELETE LATER
            userModel.setTransactionLimit(Constant.MAX_TRANSACTION);
        } else {
            throw new Exception("Nama tidak boleh kosong atau sudah digunakan");
        }

        if (password.matches(Constant.REGEX_PASSWORD)){
            userModel.setPassword(password);
        } else {
            throw new Exception("Password minimal terdiri dari 8 karakter dengan angka, huruf kapital dan kecil");
        }
        return userRepository.save(userModel);
    }

    public static String currencyFormat(BigDecimal value, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(value);
    }

    public UserDTO getInfo(String username) throws Exception{
        UserModel userModel = userRepository.findByUsername(username);
        UserDTO userDTO;
        if (userModel != null){
            userDTO = modelMapper.map(userModel, UserDTO.class);
        } else {
            throw new Exception("Id tidak ditemukan");
        }
        return userDTO;
    }

    public UserModel updateKtp(String name, UserModel um) throws Exception {
        UserModel userModel = userRepository.findByUsername(name);
        if (userModel == null){
            throw new Exception("Username tidak ditemukan");
        } else if (um.getKtp().length() != 16){
            throw new Exception("Nomor KTP harus 16 digit");
        } else if (!userRepository.findByKtpEquals(um.getKtp()).isEmpty()){  //bandingkan input dengan database
            throw new Exception("Nomor KTP sudah digunakan");
        } else {
            userModel.setKtp(um.getKtp());
            userModel.setTransactionLimit(Constant.MAX_TRANSACTION_WITH_KTP);
        }
        return userRepository.save(userModel);
    }

    public BalanceLimitDTO getBalance(String name) throws Exception{
        UserModel userModel = userRepository.findByUsername(name);
        BalanceLimitDTO balanceLimitDTO = new BalanceLimitDTO();
        if (userModel == null){
            throw new Exception("Username tidak ditemukan");
        } else {
            balanceLimitDTO.setBalance(currencyFormat(userModel.getBalance(), new Locale("in", "ID")));
            balanceLimitDTO.setTransactionLimit(currencyFormat(userModel.getTransactionLimit(), new Locale("in", "ID")));
        }
        return balanceLimitDTO;
    }

    public UserModel unBan(String name) throws Exception{
        UserModel userModel = userRepository.findByUsername(name);
        if (!userModel.getUsername().isEmpty()){
            userModel.setBanned(false);
            userModel.setPasswordAttempt(0);
        } else {
            throw new Exception("Username tidak ditemukan");
        }
        return userRepository.save(userModel);
    }

    public UserModel changePassword(UserModel um) throws Exception{
        UserModel userModel = userRepository.findByUsername(um.getUsername());
        if (userModel == null) {
            throw new Exception("Username tidak ditemukan");
        }
        String oldPass = userModel.getPassword();   //dari database
        String newPass = um.getPassword();          //dari input
        if (newPass.equals(oldPass)){
            throw new Exception("Password baru sama dengan password lama");
        } else if (!newPass.matches(Constant.REGEX_PASSWORD)){
            throw new Exception("Password minimal terdiri dari 10 karakter dengan angka, huruf kapital dan kecil, serta simbol");
        } else {
            userModel.setOldPassword(oldPass);          //set oldPass dari pass saat ini
            userModel.setPassword(newPass);            //set pass dari pass baru
        }
        return userRepository.save(userModel);
    }

}
