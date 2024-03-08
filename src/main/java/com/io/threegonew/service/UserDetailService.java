package com.io.threegonew.service;

import com.io.threegonew.config.PrincipalDetails;
import com.io.threegonew.domain.User;
import com.io.threegonew.domain.UserRole;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
//    }

@Override
public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

    User user = userRepository.findById(id).orElseThrow(() -> {
        return new UsernameNotFoundException("유저 정보를 찾을 수 없습니다.");
    });

//
//    List<GrantedAuthority> authorities = new ArrayList<>();
//
//    if(("admin").equals(id)){
//        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
//    }else {
//        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
//    }
//    //TODO:관리자 모드인것같은디
////    return new User(user.getId(), user.getPw() , authorities);
    return new PrincipalDetails(user);
}

}

