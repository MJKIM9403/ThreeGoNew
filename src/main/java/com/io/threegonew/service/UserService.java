package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .u_id(dto.getU_id())
                .u_pw(bCryptPasswordEncoder.encode(dto.getU_pw()))
                .u_name(dto.getU_name())
                .u_email(dto.getU_email())
                .u_ofile(dto.getU_ofile())
                .u_sfile(dto.getU_sfile())
                .u_about(dto.getU_about())
                .build()
        ).getU_id();
    }


}
