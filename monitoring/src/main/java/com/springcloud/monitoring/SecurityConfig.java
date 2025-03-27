package com.springcloud.monitoring;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/actuator/**").authenticated() // 나머지 actuator 엔드포인트는 인증 필요
                        .anyRequest().permitAll() // 그 외 모든 요청 인증처리
        );

        // HTTP Basic 인증 활성화 (폼 로그인 제거)
        http.httpBasic(Customizer.withDefaults()); // Basic Auth 활성화
        http.formLogin(AbstractHttpConfigurer::disable); // 폼 로그인 비활성화

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}password") // {noop}은 암호화 없이 사용
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}