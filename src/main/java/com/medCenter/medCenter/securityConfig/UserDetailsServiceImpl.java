package com.medCenter.medCenter.securityConfig;

import com.medCenter.medCenter.model.entity.User;
import com.medCenter.medCenter.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("USERname!!!!!!!!!!!!!!!!!!!!!" + username);
        Optional<User> user = userRepository.findByLogin(username);
        System.out.println("USER!!!!!!!!!!!!!!!!!!!!!" + user);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("no such login"));
    }
}
