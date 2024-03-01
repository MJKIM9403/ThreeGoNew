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


    public User authenticateUser(String id, String pw) {
        // ID를 사용하여 사용자를 검색합니다.
        Optional<User> optionalUser = userRepository.findById(id);

        // 사용자가 존재하고, 비밀번호가 일치하면 사용자를 반환합니다.
        if (optionalUser.isPresent()) {
            User user = optionalUser.orElseGet(User::new);
            System.out.println(user.getPw());
            if (bCryptPasswordEncoder.matches(pw, user.getPw())) {
                return user;
            }
        }

        // 사용자가 존재하지 않거나 비밀번호가 일치하지 않으면 null을 반환합니다.
        return null;

    // TODO : 이메일 체크
//    public boolean isEmailDuplicate(String email) {
//        return userRepository.existsByEmail(email);
//    }
}
}
