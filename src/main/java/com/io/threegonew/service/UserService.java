package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll(){
        return userRepository.findAll();
    }



    public String save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .id(dto.getId())
                .pw(bCryptPasswordEncoder.encode(dto.getPw())) // 비밀번호 해싱
                .name(dto.getName())
                .email(dto.getEmail())
                .u_ofile(dto.getU_ofile())
                .u_sfile(dto.getU_sfile())
                .u_about(dto.getU_about())
                .build()
        ).getId();
    }


    public boolean isIdDuplicate(String userId) {
        return userRepository.existsById(userId);
    }



    // TODO : 이메일 체크
//    public boolean isEmailDuplicate(String email) {
//        return userRepository.existsByEmail(email);
//    }
}
