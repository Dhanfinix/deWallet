package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.GetInfoDTO;
import com.dhandev.dewallet.dto.request.AddKtpDTO;
import com.dhandev.dewallet.dto.request.ChangePassDTO;
import com.dhandev.dewallet.dto.request.RegistDTO;
import com.dhandev.dewallet.exception.FormatInvalid;
import com.dhandev.dewallet.response.ResponseHandler;
import com.dhandev.dewallet.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    //register with unique username and password validation
    @PostMapping("/registration")
    public ResponseEntity<Object> register(@Valid @RequestBody RegistDTO registDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FormatInvalid();
        }
        userService.register(registDTO);
        return ResponseHandler.generateResponse("Berhasil Mendaftar", HttpStatus.OK, null);
    }


    //show username and ktp columns, use DTO and ModelMapper
    @GetMapping("/{username}/getinfo")
    public ResponseEntity<Object> getInfo(@PathVariable String username){
        GetInfoDTO result = userService.getInfo(username);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, result);
    }


    //change or add ktp to account based on its username and no same ktp
    @PutMapping("/{username}/addktp")
    public ResponseEntity<Object> addKtp(@PathVariable String username, @Valid @RequestBody AddKtpDTO addKtpDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FormatInvalid();
        }
        userService.updateKtp(username, addKtpDTO);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
    }

    //Get user balance and its limit
    @GetMapping("/{username}/getbalance")
    public ResponseEntity<Object> getBalance(@PathVariable String username){
        BalanceLimitDTO result = userService.getBalance(username);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, result);
    }

    //Unban user by its username
    @PutMapping("/{username}/unban")
    public ResponseEntity<Object> unBan(@PathVariable String username){
        userService.unBan(username);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
    }

    //Change user password, it should be unique and not same with the old one
    @PostMapping("/changepassword")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePassDTO changePassDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new FormatInvalid();
        }
        userService.changePassword(changePassDTO);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
    }
}
