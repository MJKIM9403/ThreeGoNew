package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.UpdateUserProfileRequest;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.FileHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailService userDetailService;
    private final FileHandler fileHandler;

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

        return userInfoMapper(findUser);
    }

    public User findUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("회원정보를 찾을 수 없습니다."));
    }


    private UserInfoResponse userInfoMapper(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getU_sfile())
                .about(user.getAbout())
                .build();
    }


    public boolean isIdDuplicate(String userId) {
        return userRepository.existsById(userId);
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

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    //    회원 수정 업데이트 처리.
    @Transactional
    public void modifyUserProfile(UpdateUserProfileRequest request) throws Exception {
        // userId를 사용하여 사용자 정보를 조회합니다.
        User user = userRepository.findById(request.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("회원정보를 찾을 수 없습니다."));
        user.update(request.getName(), request.getAbout());
        if(!request.getNewProfileImg().isEmpty()){
            fileHandler.updateUserProfile(user, request.getNewProfileImg());
        }
        if(request.getNewProfileImg() == null){
            fileHandler.deleteUserProfile(user);
        }
        sessionReset(user);
    }

    public  void sessionReset(User user) {
        Collection authorities = new ArrayList<>();
//        authorities.add(() -> user.getRole().toString());

        UserDetails userDetails = userDetailService.loadUserByUsername(user.getId());
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    @Transactional
    public void resetPassword(User modifyUser, String password){
        modifyUser.updatePw(bCryptPasswordEncoder.encode(password));
    }

    public boolean isSamePassword(User user, String password){
        return bCryptPasswordEncoder.matches(password, user.getPw());
    }

//    비밀번호 찾기 이메일 체크
    public boolean userEmailCheck(String email, String userId){
        Optional<User> userOptional = userRepository.findByEmail(email);
        // 이메일이 존재하고 사용자 ID가 일치하는지 확인
        return userOptional.isPresent() && userOptional.get().getId().equals(userId);
    }

    @Transactional
    public void updateUserPassword(String id, String newPw) {
        userRepository.findById(id).ifPresent(user ->
            user.updatePw(bCryptPasswordEncoder.encode(newPw)));
    }


}



