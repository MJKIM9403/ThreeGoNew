package com.io.threegonew.config;

import com.io.threegonew.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 권한 관련 작업을 하기 위한 role return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
//        collections.add(() -> {
//            return user.getRole().name();
//        });

        return collections;
    }

    public String getName() { return user.getName(); }

    // get Password 메서드
    @Override
    public String getPassword() {
        return user.getPw();
    }

    // get Username 메서드 (생성한 User은 loginId 사용)
    @Override
    public String getUsername() {
        return user.getId();
    }

    // 사용자의 이메일 주소를 반환하는 메서드
    public String getEmail() {
        return user.getEmail();
    }
    // 계정이 만료 되었는지 (true: 만료X)

    public String getProfileImg() {
        return user.getU_sfile();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겼는지 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되었는지 (true: 만료X)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용가능)인지 (true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
