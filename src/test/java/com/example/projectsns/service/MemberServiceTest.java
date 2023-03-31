package com.example.projectsns.service;

import com.example.projectsns.dto.request.SignInRequest;
import com.example.projectsns.dto.request.SignUpRequest;
import com.example.projectsns.exception.ErrorCode;
import com.example.projectsns.exception.ProjectSnsException;
import com.example.projectsns.model.entity.Member;
import com.example.projectsns.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 회원가입_정상() {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignUpRequest request = SignUpRequest.of(memberId, password);

        //mocking
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encrypt_test123!@#");
        when(memberRepository.save(request.toEntity())).thenReturn(Member.of(memberId, password));

        //then
        assertDoesNotThrow(() -> memberService.signUp(request));
    }

    @Test
    void 회원가입_실패_memberId_중복() {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignUpRequest request = SignUpRequest.of(memberId, password);

        memberService.signUp(request);

        //mocking
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(request.toEntity()));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encrypt_test123!@#");

        //then
        ProjectSnsException e = assertThrows(ProjectSnsException.class, () -> memberService.signUp(request));
        assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_MEMBER_ID);
        assertEquals(e.getMessage(), String.format("%s -> Member id `%s` is duplicated", ErrorCode.DUPLICATED_MEMBER_ID, memberId));
    }

    @Test
    void 로그인_정상() {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        memberService.signUp(SignUpRequest.of(memberId, password));

        //mocking
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(request.toEntity()));
        when(bCryptPasswordEncoder.matches(password, "encrypt_test123!@#")).thenReturn(true);

        //then
        assertDoesNotThrow(() -> memberService.signIn(request));
    }

    @Test
    void 로그인_실패_미가입_memberId() {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        //mocking
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //then
        ProjectSnsException e = assertThrows(ProjectSnsException.class, () -> memberService.signIn(request));
        assertEquals(e.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        assertEquals(e.getMessage(), String.format("%s -> Member id `%s` is not founded", ErrorCode.MEMBER_NOT_FOUND, memberId));
    }

    @Test
    void 로그인_실패_password_불일치() {
        //given
        String memberId = "test";
        String password = "test123!@#";
        String wrongPassword = "wrong_test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        memberService.signUp(SignUpRequest.of(memberId, password));

        //mocking
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(request.toEntity()));
        when(bCryptPasswordEncoder.matches(wrongPassword, "encrypt_test123!@#")).thenReturn(false);

        //then
        ProjectSnsException e = assertThrows(ProjectSnsException.class, () -> memberService.signIn(SignInRequest.of(memberId, wrongPassword)));
        assertEquals(e.getErrorCode(), ErrorCode.INVALID_PASSWORD);
        assertEquals(e.getMessage(), String.format("%s -> Password is invalid", ErrorCode.INVALID_PASSWORD));
    }
}