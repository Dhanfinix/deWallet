package com.dhandev.dewallet.controller;

import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.UserDTO;
import com.dhandev.dewallet.dto.request.AddKtpDTO;
import com.dhandev.dewallet.dto.request.ChangePassDTO;
import com.dhandev.dewallet.dto.request.RegistDTO;
import com.dhandev.dewallet.model.UserModel;
import com.dhandev.dewallet.response.ResponseHandler;
import com.dhandev.dewallet.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    //register with unique username and password validation
    @PostMapping("/registration")
    public ResponseEntity<Object> register(@RequestBody RegistDTO registDTO){
        try {
            userService.register(registDTO);
            return ResponseHandler.generateResponse("Berhasil Mendaftar", HttpStatus.OK, null);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    //show username and ktp columns, use DTO and ModelMapper
    @GetMapping("/{username}/getinfo")
    public ResponseEntity<Object> getInfo(@PathVariable String username){
        try {
            UserDTO result = userService.getInfo(username);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, result);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    //change or add ktp to account based on its username and no same ktp
    @PutMapping("/{username}/addktp")
    public ResponseEntity<Object> addKtp(@PathVariable String username, @RequestBody AddKtpDTO addKtpDTO){
        try {
            UserModel result = userService.updateKtp(username, addKtpDTO);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    //Get user balance and its limit
    @GetMapping("/{username}/getbalance")
    public ResponseEntity<Object> getBalance(@PathVariable String username){
        try {
            BalanceLimitDTO result = userService.getBalance(username);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, result);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    //Unban user by its username
    @PutMapping("/{username}/unban")
    public ResponseEntity<Object> unBan(@PathVariable String username){
        try{
            userService.unBan(username);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    //Change user password, it should be unique and not same with the old one
    @PostMapping("/changepassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePassDTO changePassDTO){
        try{
            userService.changePassword(changePassDTO);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, null);
        } catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
