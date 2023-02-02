package com.dhandev.dewallet.service;

import com.dhandev.dewallet.constant.Constant;
import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.GetInfoDTO;
import com.dhandev.dewallet.dto.request.AddKtpDTO;
import com.dhandev.dewallet.dto.request.ChangePassDTO;
import com.dhandev.dewallet.dto.request.RegistDTO;
import com.dhandev.dewallet.exception.*;
import com.dhandev.dewallet.exception.transaction.*;
import com.dhandev.dewallet.exception.user.*;
import com.dhandev.dewallet.mapper.UserMapper;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@Transactional(dontRollbackOn = {
        NoUserFoundException.class,
        AccountBlocked.class,
        WrongPassword.class,
        NewPassEqualOldPass.class,
        PassFormatInvalid.class,
        FormatInvalid.class,
        UsernameAlreadyUsed.class,
        KtpFormatInvalid.class,
        KtpAlreadyUsed.class,
})
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void register(RegistDTO registDTO) {
        UserModel userModel;
        if (!userRepository.findByUsernameEquals(registDTO.getUsername()).isEmpty()){
            throw new UsernameAlreadyUsed();
        } else if (!registDTO.getPassword().matches(Constant.REGEX_PASSWORD)){
            throw new PassFormatInvalid();
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

    public GetInfoDTO getInfo(String username){
        UserModel userModel = userRepository.findByUsername(username);
        GetInfoDTO getInfoDTO;
        if (userModel != null){
            getInfoDTO = userMapper.toUserDto(userModel);
        } else {
            throw new NoUserFoundException();
        }
        return getInfoDTO;
    }

    public UserModel updateKtp(String name, AddKtpDTO addKtpDTO) {
        UserModel userModel = userRepository.findByUsername(name);

        if (userModel == null){
            throw new NoUserFoundException();
        } else if (!addKtpDTO.getKtp().matches(Constant.REGEX_KTP)){
            throw new KtpFormatInvalid();
        } else if (!userRepository.findByKtpEquals(addKtpDTO.getKtp()).isEmpty()){  //bandingkan input dengan database
            throw new KtpAlreadyUsed();
        } else {
            //To update, need the same id to use mapper
            addKtpDTO.setId(userModel.getId());
            userModel = userMapper.AddKtpToUserModel(addKtpDTO, userModel);
            userModel.setTransactionLimit(Constant.MAX_TRANSACTION_WITH_KTP);
        }
        return userRepository.save(userModel);
    }

    public BalanceLimitDTO getBalance(String name){
        UserModel userModel = userRepository.findByUsername(name);
        BalanceLimitDTO balanceLimitDTO = new BalanceLimitDTO();
        if (userModel == null){
            throw new NoUserFoundException();
        } else {
            balanceLimitDTO = userMapper.toBalanceLimitDto(userModel);
        }
        return balanceLimitDTO;
    }

    public void unBan(String name){
        UserModel userModel = userRepository.findByUsername(name);
        if (userModel != null){
            userModel.setBanned(false);
            userModel.setPasswordAttempt(0);
        } else {
            throw new NoUserFoundException();
        }
        userRepository.save(userModel);
    }

    public void changePassword(ChangePassDTO changePassDTO) {
        UserModel userModel = userRepository.findByUsername(changePassDTO.getUsername());
        if (userModel == null) {
            throw new NoUserFoundException();
        }
        String oldPass = userModel.getPassword();   //dari database
        String newPass = changePassDTO.getPassword();          //dari input
        int attempt = userModel.getPasswordAttempt();

        //check old pass di database sama dengan old pass yang dideklarasikan di request body
        if (!oldPass.equals(changePassDTO.getOldPassword())){
            if (attempt==3){
                userModel.setBanned(true);
                throw new AccountBlocked();
            }
            userModel.setPasswordAttempt(attempt+1);
            throw new WrongPassword();
        } else if (userModel.isBanned()){
            throw new AccountBlocked();
        }
        userModel.setPasswordAttempt(0);

        //check antar input old dan new harus beda
        if (newPass.equals(oldPass)){
            throw new NewPassEqualOldPass();
        } else if (!newPass.matches(Constant.REGEX_PASSWORD)){
            throw new PassFormatInvalid();
        } else {
            userMapper.ChangePassToUserModel(changePassDTO, userModel);
        }
        userRepository.save(userModel);
    }

}
