package com.example.projectsns.configuration;

import com.example.projectsns.configuration.filter.JwtTokenFilter;
import com.example.projectsns.configuration.handler.CustomAuthenticationEntryPoint;
import com.example.projectsns.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    /**
     * Spring security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //BEGIN> JWT 인증 사용으로 불필요한 옵션 비활성화
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //<END
                .and()
                .authorizeHttpRequests(auth -> {
                            try {
                                auth
                                        //static resources 에 대한 접근 허용
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/favicon.io", "/css/**", "/js/**", "/img/**").permitAll()
                                        //보안 및 회원가입, 로그인 페이지에 대한 접근 허용
                                        .requestMatchers("/secure", "/users/join", "/users/login").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        //JWT 토큰 필터를 등록해 사용자 인증 처리
                                        .addFilterBefore(new JwtTokenFilter(customUserDetailsService, jwtSecretKey), UsernamePasswordAuthenticationFilter.class)
                                        //security 예외 핸들러를 등록해 예외 처리
                                        .exceptionHandling()
                                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .build();
    }

    /**
     * Password encoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
