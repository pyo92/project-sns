package com.example.projectsns.service.auth;

import com.example.projectsns.exception.CustomErrorCode;
import com.example.projectsns.exception.CustomException;
import com.example.projectsns.dto.auth.CustomUserDetails;
import com.example.projectsns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(CustomUserDetails::from)
                .orElseThrow(() -> CustomException.of(CustomErrorCode.USER_NOT_FOUND));
    }
}
