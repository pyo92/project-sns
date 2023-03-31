package com.example.projectsns.controller;

import com.example.projectsns.dto.request.SignInRequest;
import com.example.projectsns.dto.request.SignUpRequest;
import com.example.projectsns.dto.response.Response;
import com.example.projectsns.dto.response.SignInResponse;
import com.example.projectsns.dto.response.SignUpResponse;
import com.example.projectsns.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public Response<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        return Response.success(memberService.signUp(request));
    }

    @PostMapping("/sign-in")
    public Response<SignInResponse> signIn(@RequestBody SignInRequest request) {
        return Response.success(memberService.signIn(request));
    }
}
