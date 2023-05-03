package com.example.projectsns.service;

import com.example.projectsns.dto.UserDto;
import com.example.projectsns.dto.request.JoinRequest;
import com.example.projectsns.dto.request.LoginRequest;
import com.example.projectsns.dto.response.JoinResponse;
import com.example.projectsns.dto.response.LoginResponse;
import com.example.projectsns.exception.CustomErrorCode;
import com.example.projectsns.exception.CustomException;
import com.example.projectsns.model.entity.User;
import com.example.projectsns.repository.UserRepository;
import com.example.projectsns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.expired.time}")
    private Long jwtExpiredTimeMs;

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

    /**
     * 로그인 비즈니스 로직 처리
     */
    public LoginResponse login(LoginRequest request) {
        //회원가입 여부 체크
        User findUser = userRepository.findById(request.email())
                .orElseThrow(() -> CustomException.of(CustomErrorCode.INVALID_LOGIN_INFO, request));

        //비밀번호 체크
        if (!bCryptPasswordEncoder.matches(request.password(), findUser.getPassword())) {
            throw CustomException.of(CustomErrorCode.INVALID_LOGIN_INFO, request);
        }

        //토큰 생성
        String jwt = JwtTokenUtils.generateToken(request.email(), jwtSecretKey, jwtExpiredTimeMs);

        return LoginResponse.of(jwt);
    }

    /**
     * 타 서비스 계층에서의 회원 여부 검증 비즈니스 로직 처리
     */
    @Transactional(readOnly = true)
    public boolean verifyUser(UserDto dto) {
        return userRepository.findById(dto.email()).isPresent();
    }
}
