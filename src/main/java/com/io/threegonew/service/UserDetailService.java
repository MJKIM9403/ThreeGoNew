package com.io.threegonew.service;

import com.io.threegonew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
//    }

//@Override
//public UserDetails loadUserByUsername(String id) {
//    if (id == null || id.trim().isEmpty()) {
//        throw new IllegalArgumentException("User ID cannot be null or empty");
//    }
//
//    return userRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
//}

@Override
public UserDetails loadUserByUsername(String id) {
    System.out.println(id);

    if (id == null || id.trim().isEmpty()) {
        throw new IllegalArgumentException("User ID cannot be null or empty");

    }

    return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

}
}

