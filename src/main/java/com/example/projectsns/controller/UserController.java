package com.example.projectsns.controller;

import com.example.projectsns.dto.request.JoinRequest;
import com.example.projectsns.dto.request.LoginRequest;
import com.example.projectsns.dto.response.JoinResponse;
import com.example.projectsns.dto.response.LoginResponse;
import com.example.projectsns.exception.CustomErrorCode;
import com.example.projectsns.exception.CustomException;
import com.example.projectsns.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 Form view - GET 요청 처리
     */
    @GetMapping("/join")
    public String joinForm(ModelMap map) {
        //invalid 시, 입력 값 유지를 위해 input 데이터를 바인딩하고 있어서
        //해당 view 에 대한 정상적인 resolve 를 위해 최초 요청 시, empty 객체를 전달한다.
        map.addAttribute("joinRequest", JoinRequest.of());
        return "users/join";
    }

    /**
     * 회원가입 Form - POST 요청 처리
     */
    @PostMapping(value = "/join")
    public String join(@Valid JoinRequest request, BindingResult bindingResult, ModelMap map, HttpSession session) {
        //server side 유효성 검사 오류 처리
        if (bindingResult.hasErrors()) {
            throw CustomException.of(CustomErrorCode.INVALID_INPUT_VALUE, request);
        }

        //회원가입 처리
        JoinResponse response = userService.join(request);

        //로그인 Form - email 및 가입 완료 alert 표시용 session 값 binding
        session.setAttribute("joinEmail", response.email());
        session.setAttribute("joinSuccess", true);

        return "redirect:/users/login";
    }

    /**
     * 로그인 Form - GET 요청 처리
     */
    @GetMapping("/login")
    public String loginForm(HttpSession session, ModelMap map) {
        //회원가입 직후, 가입 완료 alert 표시용 model 세팅
        Object joinSuccess = session.getAttribute("joinSuccess");
        if (joinSuccess != null) {
            map.addAttribute("joinSuccess", joinSuccess);
            session.removeAttribute("joinSuccess");
        }

        //회원가입 직후, 입력했던 email input 바인딩용 model 세팅
        Object joinEmail = session.getAttribute("joinEmail");
        if (joinEmail == null) {
            map.addAttribute("loginRequest", LoginRequest.of());
        } else {
            map.addAttribute("loginRequest", LoginRequest.of(session.getAttribute("joinEmail").toString()));
            session.removeAttribute("joinEmail");
        }

        //JWT filter 예외 발생 시, 세션 만료 alert 표시용 model 세팅
        Object jwtExpired = session.getAttribute("jwtExpired");
        if (jwtExpired != null) {
            map.addAttribute("jwtExpired", jwtExpired);
            session.removeAttribute("jwtExpired");
        }

        return "users/login";
    }

    /**
     * 로그인 Form - POST 요청 처리
     */
    @PostMapping(value = "/login")
    public String login(LoginRequest request, HttpServletResponse response) {
        //로그인 처리
        LoginResponse login = userService.login(request);

        //로그인 response - JWT token 을 cookie 에 세팅
        Cookie cookie = new Cookie("JWT", login.jwt());
        cookie.setPath("/");
        cookie.setHttpOnly(true); //js 에서 해당 cookie 접근 불가 처리 (서버에서만 사용하므로)
        cookie.setSecure(true); //https 프로토콜 환경에서만 해당 cookie 전송

        response.addCookie(cookie);

        return "redirect:/";
    }
}
