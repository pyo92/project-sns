package com.example.projectsns.controller;

import com.example.projectsns.dto.request.JoinRequest;
import com.example.projectsns.dto.response.JoinResponse;
import com.example.projectsns.service.UserService;
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
        //server side validate - invalid 처리
        if (bindingResult.hasErrors()) {
            //입력 값 유지를 위해 model 에 request dto 바인딩
            map.addAttribute("joinRequest", request);
            return "users/join";
        }

        //회원가입 처리
        JoinResponse response = userService.join(request);

        //로그인 Form - email 및 가입 완료 alert 표시용 session 값 binding
        session.setAttribute("joinEmail", response.email());
        session.setAttribute("joinSuccess", true);

        return "redirect:/users/login";
    }
}
