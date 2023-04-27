package com.example.projectsns.service;

import com.example.projectsns.dto.request.JoinRequest;
import com.example.projectsns.dto.response.JoinResponse;
import com.example.projectsns.exception.CustomErrorCode;
import com.example.projectsns.exception.CustomException;
import com.example.projectsns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    /**
     * 회원가입 비즈니스 로직 처리
     */
    public JoinResponse join(JoinRequest request) {
        //email 중복 체크
        userRepository.findById(request.email()).ifPresent(u -> {
            throw CustomException.of(CustomErrorCode.DUPLICATED_EMAIL, request);
        });

        //nickname 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(u -> {
            throw CustomException.of(CustomErrorCode.DUPLICATED_NICKNAME, request);
        });

        //비밀번호 암호화 + 회원가입 처리
        String encryptedPassword = bCryptPasswordEncoder.encode(request.password());
        return JoinResponse.from(userRepository.save(JoinRequest.of(request.email(), encryptedPassword, request.nickname()).toEntity()));
    }
}
