package com.dhandev.dewallet.service;

import com.dhandev.dewallet.constant.Constant;
import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.UserDTO;
import com.dhandev.dewallet.dto.request.AddKtpDTO;
import com.dhandev.dewallet.dto.request.ChangePassDTO;
import com.dhandev.dewallet.dto.request.RegistDTO;
import com.dhandev.dewallet.mapper.UserMapper;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserMapper userMapper;

    public void register(RegistDTO registDTO) throws Exception {
        UserModel userModel;
        if (registDTO.getUsername().isEmpty()){
            throw new Exception("Nama tidak boleh kosong ");
        } else if (!userRepository.findByUsernameEquals(registDTO.getUsername()).isEmpty()){
            throw new Exception("Nama sudah digunakan");
        } else if (!registDTO.getPassword().matches(Constant.REGEX_PASSWORD)){
            throw new Exception("Password minimal terdiri dari 10 karakter dengan angka, huruf kapital dan kecil");
        } else {
            userModel = userMapper.RegisToUserModel(registDTO);
            userModel.setBalance(BigDecimal.valueOf(0));
            userModel.setTransactionLimit(Constant.MAX_TRANSACTION);
        }
        userRepository.save(userModel);
    }

    public static String currencyFormat(BigDecimal value, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(value);
    }

    public UserDTO getInfo(String username) throws Exception{
        UserModel userModel = userRepository.findByUsername(username);
        UserDTO userDTO;
        if (userModel != null){
            userDTO = userMapper.toUserDto(userModel);
        } else {
            throw new Exception("Username tidak ditemukan");
        }
        return userDTO;
    }

    public UserModel updateKtp(String name, AddKtpDTO addKtpDTO) throws Exception {
        UserModel userModel = userRepository.findByUsername(name);

        if (userModel == null){
            throw new Exception("Username tidak ditemukan");
        } else if (!addKtpDTO.getKtp().matches(Constant.REGEX_KTP)){
            throw new Exception("Nomor KTP harus 16 digit dan tidak mengandung huruf");
        } else if (!userRepository.findByKtpEquals(addKtpDTO.getKtp()).isEmpty()){  //bandingkan input dengan database
            throw new Exception("Nomor KTP sudah digunakan");
        } else {
            //To update, need the same id to use mapper
            addKtpDTO.setId(userModel.getId());
            userModel = userMapper.AddKtpToUserModel(addKtpDTO, userModel);
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
            balanceLimitDTO = userMapper.toBalanceLimitDto(userModel);
        }
        return balanceLimitDTO;
    }

    public void unBan(String name) throws Exception{
        UserModel userModel = userRepository.findByUsername(name);
        if (!userModel.getUsername().isEmpty()){
            userModel.setBanned(false);
            userModel.setPasswordAttempt(0);
        } else {
            throw new Exception("Username tidak ditemukan");
        }
        userRepository.save(userModel);
    }

    public void changePassword(ChangePassDTO changePassDTO) throws Exception{
        UserModel userModel = userRepository.findByUsername(changePassDTO.getUsername());
        if (userModel == null) {
            throw new Exception("Username tidak ditemukan");
        }
        String oldPass = userModel.getPassword();   //dari database
        String newPass = changePassDTO.getPassword();          //dari input
        int attempt = userModel.getPasswordAttempt();

        //check old pass di database sama dengan old pass yang dideklarasikan di request body
        if (!oldPass.equals(changePassDTO.getOldPassword())){
            if (attempt==3){
                userModel.setBanned(true);
                throw new Exception("Akun anda terblokir");
            }
            userModel.setPasswordAttempt(attempt+1);
            throw new Exception("Password salah");
        } else if (userModel.isBanned()){
            throw new Exception("Akun anda terblokir");
        }

        //check antar input old dan new harus beda
        if (newPass.equals(oldPass)){
            throw new Exception("Password baru sama dengan password lama");
        } else if (!newPass.matches(Constant.REGEX_PASSWORD)){
            throw new Exception("Password minimal terdiri dari 10 karakter dengan angka, huruf kapital dan kecil, serta simbol");
        } else {
            userMapper.ChangePassToUserModel(changePassDTO, userModel);
        }
        userRepository.save(userModel);
    }

}
