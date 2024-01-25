package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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


//    public String login(LoginRequest loginRequest) {
//        String id = loginRequest.getId();
//        String pw = loginRequest.getPw();
//
//        Optional<User> byId = userRepository.findById(id);
//
//        if (byId.isPresent()) {
//            if (bCryptPasswordEncoder.matches(pw, byId.get().getPw())) {
//                return "로그인 성공";
//            } else {
//                return "비밀번호 불일치";
//            }
//        } else {
//            return "사용자가 존재하지 않습니다";
//        }
//    }


    public boolean isIdDuplicate(String userId) {
        return userRepository.existsById(userId);
    }

    // TODO : 이메일 체크
//    public boolean isEmailDuplicate(String email) {
//        return userRepository.existsByEmail(email);
//    }

}
