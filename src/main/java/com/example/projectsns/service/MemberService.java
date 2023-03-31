package com.example.projectsns.service;

import com.example.projectsns.dto.response.SignInResponse;
import com.example.projectsns.exception.ErrorCode;
import com.example.projectsns.model.entity.Member;
import com.example.projectsns.dto.request.SignInRequest;
import com.example.projectsns.dto.request.SignUpRequest;
import com.example.projectsns.dto.response.SignUpResponse;
import com.example.projectsns.exception.ProjectSnsException;
import com.example.projectsns.repository.MemberRepository;
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
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.expired.time}")
    private Long jwtExpiredTimeMs;

    public SignUpResponse signUp(SignUpRequest request) {
        //memberId 중복 체크
        memberRepository.findById(request.memberId()).ifPresent(m -> {
            throw new ProjectSnsException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("Member id `%s` is duplicated", request.memberId()));
        });

        //비밀번호 암호화 + 회원가입 처리
        return SignUpResponse.from(memberRepository.save(SignUpRequest.of(request.memberId(), bCryptPasswordEncoder.encode(request.password())).toEntity()));
    }

    public SignInResponse signIn(SignInRequest request) {
        //회원가입 여부 체크
        Member findMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new ProjectSnsException(ErrorCode.MEMBER_NOT_FOUND, String.format("Member id `%s` is not founded", request.memberId())));

        //비밀번호 체크
        if (!bCryptPasswordEncoder.matches(request.password(), findMember.getPassword())) {
            throw new ProjectSnsException(ErrorCode.INVALID_PASSWORD, "Password is invalid");
        }

        //토큰 생성
        return SignInResponse.of(JwtTokenUtils.generateToken(request.memberId(), jwtSecretKey, jwtExpiredTimeMs));
    }
}
