package com.example.projectsns.controller;

import com.example.projectsns.dto.request.SignInRequest;
import com.example.projectsns.dto.request.SignUpRequest;
import com.example.projectsns.dto.response.SignInResponse;
import com.example.projectsns.dto.response.SignUpResponse;
import com.example.projectsns.exception.ErrorCode;
import com.example.projectsns.exception.ProjectSnsException;
import com.example.projectsns.model.MemberRole;
import com.example.projectsns.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @Test
    void 회원가입_정상() throws Exception {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignUpRequest request = SignUpRequest.of(memberId, password);

        //when
        when(memberService.signUp(request)).thenReturn(SignUpResponse.of(memberId, MemberRole.MEMBER));

        //then
        mockMvc.perform(post("/api/v1/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SignUpRequest.of(memberId, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입_실패_memberId_중복() throws Exception {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignUpRequest request = SignUpRequest.of(memberId, password);

        //when
        when(memberService.signUp(request)).thenThrow(new ProjectSnsException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("Member id `%s` is duplicated", memberId)));

        //then
        mockMvc.perform(post("/api/v1/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SignUpRequest.of(memberId, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void 로그인_정상() throws Exception {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        //when
        when(memberService.signIn(request)).thenReturn(SignInResponse.of("test_token"));

        //then
        mockMvc.perform(post("/api/v1/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SignInRequest.of(memberId, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 로그인_실패_미가입_memberId() throws Exception {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        //when
        when(memberService.signIn(request)).thenThrow(new ProjectSnsException(ErrorCode.MEMBER_NOT_FOUND, String.format("Member id `%s` is not founded", memberId)));

        //then
        mockMvc.perform(post("/api/v1/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SignInRequest.of(memberId, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void 로그인_실패_password_불일치() throws Exception {
        //given
        String memberId = "test";
        String password = "test123!@#";
        SignInRequest request = SignInRequest.of(memberId, password);

        //when
        when(memberService.signIn(request)).thenThrow(new ProjectSnsException(ErrorCode.INVALID_PASSWORD, "Password is invalid"));

        //then
        mockMvc.perform(post("/api/v1/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SignInRequest.of(memberId, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}