package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.io.threegonew.domain.User;

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
                .about(dto.getAbout())
                .build()
        ).getId();
    }

    public UserInfoResponse findUserInfo(String userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        return userInfoResponse(findUser);
    }

    public User findUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("회원정보를 찾을 수 없습니다."));
    }


    private UserInfoResponse userInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg("../assets/img/profileimg/" + user.getU_sfile())
                .about(user.getAbout())
                .build();
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

    public User findUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // 만약 사용자가 존재하지 않는다면 null 반환
    }


    // 현재 인증된 사용자의 아이디 반환
    public String getCurrentUserId() {
        // 현재 인증된 사용자의 정보를 SecurityContextHolder에서 가져와서 반환
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // 현재 인증된 사용자의 이메일 주소 반환
    public String getCurrentUserEmail() {
        // 현재 인증된 사용자의 아이디를 가져옴
        String userId = getCurrentUserId();
        // userId를 사용하여 사용자 정보를 조회하여 이메일 주소를 반환
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return user.getEmail();
    }

    public String getCurrentUserName(){
        String userId = getCurrentUserId(); // 현재 사용자의 아이디를 가져옴
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getName();
    }

    public String getCurrentUserAbout(){
        String userId = getCurrentUserId(); // 현재 사용자의 아이디를 가져옴
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getAbout();
    }

//    회원 수정 업데이트 처리.
    @Transactional
    public void modify(){

    }

}



