package com.vr.authservice.mapper;

import com.vr.authservice.dto.requests.SignUpRequest;
import com.vr.authservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignUpRequest signUpRequest);
}
