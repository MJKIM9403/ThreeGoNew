package com.io.threegonew.service;

import com.io.threegonew.domain.User;
import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
public UserDetails loadUserByUsername(String id) {

    return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
}

//@Override
//public UserDetails loadUserByUsername(String email){
//    System.out.println("userdetailservice : " + email);
////
////    if (id == null || id.trim().isEmpty()) {
////        System.out.println("Invalid User ID");
////        throw new IllegalArgumentException("User ID cannot be null or empty");
////
////    }
//    return userRepository.findByEmail(email)
//            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + email));
//
//}
}

