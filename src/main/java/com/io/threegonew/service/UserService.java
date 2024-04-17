package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.*;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.FileHandler;
import com.io.threegonew.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final ModelMapper modelMapper;

    public List<User> findAll() {
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

    @Transactional
    public PageResponse<UserWithFollowStateResponse> getUserByStartOrContainUserId(String keyword, int pageNum){
        Pageable pageable = PageRequest.of(pageNum, 12);

        String startKeyword = keyword + "%";
        String containKeyword = "%" + keyword + "%";

        String loginUserId = SecurityUtils.getCurrentUsername();

        Page<UserWithFollowStateResponse> page = userRepository.findUsersByStartOrContainUserId(pageable, startKeyword, containKeyword, loginUserId)
                .map(userWithFollowStateInterface -> modelMapper.map(userWithFollowStateInterface, UserWithFollowStateResponse.class));

        PageResponse<UserWithFollowStateResponse> pageResponse = PageResponse.<UserWithFollowStateResponse>withAll()
                .dtoList(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();

        return pageResponse;
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

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // 만약 사용자가 존재하지 않는다면 null 반환
    }


    // 현재 인증된 사용자의 아이디 반환
    public String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        // 인증된 유저일 경우 principal에서 유저 id를, 비인증 유저일 경우 anonymousUser 반환
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }else {
            username = principal.toString();
        }
        return username;
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
        if (request.getNewProfileImg() != null) {
            fileHandler.updateUserProfile(user, request.getNewProfileImg());
        }
        sessionReset(user);
    }

    public void sessionReset(User user) {
        Collection authorities = new ArrayList<>();
//        authorities.add(() -> user.getRole().toString());

        UserDetails userDetails = userDetailService.loadUserByUsername(user.getId());
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    @Transactional
    public void resetPassword(User modifyUser, String password) {
        modifyUser.updatePw(bCryptPasswordEncoder.encode(password));
    }

    public boolean isSamePassword(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPw());
    }

    //    비밀번호 찾기 이메일 체크
    public boolean userEmailCheck(String email, String userId) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        // 이메일이 존재하고 사용자 ID가 일치하는지 확인
        return userOptional.isPresent() && userOptional.get().getId().equals(userId);
    }

    @Transactional
    public void updateUserPassword(String id, String newPw) {
        userRepository.findById(id).ifPresent(user ->
                user.updatePw(bCryptPasswordEncoder.encode(newPw)));
    }

    @Transactional
    public void deleteUser(String userId) throws Exception {
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 삭제
        userRepository.delete(user);

    }
}



