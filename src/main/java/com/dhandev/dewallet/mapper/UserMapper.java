package com.dhandev.dewallet.mapper;

import com.dhandev.dewallet.dto.BalanceLimitDTO;
import com.dhandev.dewallet.dto.UserDTO;
import com.dhandev.dewallet.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(UserModel userModel);

    @Mapping(target = "balance", source = "balance", numberFormat = "Rp###,###")
    @Mapping(target = "transactionLimit", source = "transactionLimit", numberFormat = "Rp###,###")
    BalanceLimitDTO toBalanceLimitDto(UserModel userModel);
}
