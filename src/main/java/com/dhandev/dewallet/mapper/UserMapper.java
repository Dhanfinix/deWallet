package com.dhandev.dewallet.mapper;

import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.UserDTO;
import com.dhandev.dewallet.dto.request.AddKtpDTO;
import com.dhandev.dewallet.dto.request.ChangePassDTO;
import com.dhandev.dewallet.dto.request.RegistDTO;
import com.dhandev.dewallet.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    UserModel RegisToUserModel(RegistDTO registDTO);

    UserModel AddKtpToUserModel(AddKtpDTO addKtpDTO, @MappingTarget UserModel userModel);

    UserDTO toUserDto(UserModel userModel);

    UserModel ChangePassToUserModel(ChangePassDTO changePassDTO, @MappingTarget UserModel userModel);

    @Mapping(target = "balance", source = "balance", numberFormat = "Rp###,###")
    @Mapping(target = "transactionLimit", source = "transactionLimit", numberFormat = "Rp###,###")
    BalanceLimitDTO toBalanceLimitDto(UserModel userModel);



}
