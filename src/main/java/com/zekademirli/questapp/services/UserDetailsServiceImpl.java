package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.UserRepository;
import com.zekademirli.questapp.security.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username);
        return JwtUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user=userRepository.findById(id).get();
        return JwtUserDetails.create(user);
    }
}
