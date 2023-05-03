package com.example.projectsns.configuration.filter;

import com.example.projectsns.exception.CustomErrorCode;
import com.example.projectsns.exception.CustomException;
import com.example.projectsns.service.auth.CustomUserDetailsService;
import com.example.projectsns.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userService;

    private final String secretKey;

    /**
     * JWT 필터 처리 제외 (static resources)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //css, js, img 제외
        return request.getRequestURI().matches("^/(css|js|img).*");
    }

    /**
     * JWT 필터 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        //secret mode 와 같이 cookie == null 인 경우
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("JWT")).findAny();

        //'JWT' cookie 가 없는 경우
        if (jwtCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = jwtCookie.get().getValue();

            //JWT expired 된 경우
            if (JwtTokenUtils.isExpired(jwt, secretKey)) {
                log.error("JWT token is expired.");
                filterChain.doFilter(request, response);
                throw CustomException.of(CustomErrorCode.INVALID_TOKEN);
            }

            //JWT token 으로 인증 처리
            String memberId = JwtTokenUtils.getEmail(jwt, secretKey);
            UserDetails userDetails = userService.loadUserByUsername(memberId);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //Security context 에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            //expired 등 JWT 예외가 발생했을 경우, cookie 에서 JWT 삭제
            Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("JWT")).findAny().ifPresent(cookie -> {
                cookie.setValue("");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            });

            //세션 만료 플래그를 session 에 바인딩
            HttpSession session = request.getSession();
            session.setAttribute("jwtExpired", true);

            log.error("Error occurs while JWT token validating. {}", e.toString());
            throw CustomException.of(CustomErrorCode.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}
