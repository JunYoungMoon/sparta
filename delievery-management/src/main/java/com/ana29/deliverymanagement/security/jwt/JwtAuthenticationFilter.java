package com.ana29.deliverymanagement.security.jwt;

import com.ana29.deliverymanagement.security.UserDetailsImpl;
import com.ana29.deliverymanagement.security.constant.jwt.JwtConfigEnum;
import com.ana29.deliverymanagement.user.constant.UserRoleEnum;
import com.ana29.deliverymanagement.user.dto.SigninRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
//      default로 post 옵션만 적용됨
//        setFilterProcessesUrl를 주석 처리하면, 해당 필터는 기본값 또는 보안 설정에서 지정한 로그인 처리 URL을 사용하게 됩니다.
//        기본적으로 UsernamePasswordAuthenticationFilter의 기본 로그인 처리 URL은 /login이지만, Spring Security의 SecurityFilterChain에서
//        formLogin() 설정을 통해 로그인 페이지 및 처리 URL을 커스터마이징한 경우, 해당 설정값이 자동으로 필터에 반영됩니다.//
        setFilterProcessesUrl("/api/users/sign-in");
    }

//    UsernamePasswordAuthenticationFilter의 동작 방식
//    UsernamePasswordAuthenticationFilter는 **"로그인 요청을 처리하는 필터"**입니다.
//            securityFilterChain에서 /api/users/sign-in 경로에 매핑된 필터이기 때문에, 로그인 요청이 아닌 다른 요청은 이 필터를 타지 않습니다.
//            따라서, 다른 GET 요청(/api/something-else)은 영향받지 않습니다.

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // POST 방식일때만 동작
//           ObjectMapper의 valueType이 매핑이 안 될시 No content to map due to end-of-input 에러 호출
//            Get 방식은 body가 비어 있으므로 LoginRequestDto 매핑이 null로 되어 위 에러 발생
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            log.info("attemptAuthentication method ");
//            아이디, 패스워드 정규식 검사
            SigninRequestDto requestDto = validLoginForm(request);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getId(),
                            requestDto.getPassword()
                    )
            );
        }

        return null;
    }

    protected SigninRequestDto validLoginForm(HttpServletRequest request) {
        try {
            SigninRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), SigninRequestDto.class);

            // 수동으로 Bean Validation 수행
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SigninRequestDto>> violations = validator.validate(requestDto);

            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; "));
                log.error("Validation failed: {}", errorMessage);
                throw new RuntimeException("Validation failed: " + errorMessage);
            }
            log.info("Validated id: {}", requestDto.getId());
            return requestDto;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtConfigEnum.AUTHORIZATION_HEADER.getGetJwtConfig(), token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}