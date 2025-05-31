package com.medCenter.medCenter.securityConfig;

import com.medCenter.medCenter.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class UserDetailsImpl implements UserDetails {

    private final User user;
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return user.getUserCredentials().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserCredentials().getLogin();
    }

    public String getName() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }

}
