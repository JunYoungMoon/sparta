package com.ana29.deliverymanagement.security.jwt;


import com.ana29.deliverymanagement.security.constant.jwt.JwtConfigEnum;
import com.ana29.deliverymanagement.security.constant.jwt.JwtErrorMessage;
import com.ana29.deliverymanagement.user.constant.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        log.info(Arrays.toString(bytes));
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return (JwtConfigEnum.BEARER_PREFIX.getGetJwtConfig() +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(JwtConfigEnum.AUTHORIZATION_HEADER.getGetJwtConfig(), role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + Long.parseLong(JwtConfigEnum.TOKEN_TIME.getGetJwtConfig()))) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact());
//                .substring(Integer.parseInt(JwtConfigEnum.BEARER_PREFIX_COUNT.getGetJwtConfig()));
        // BEARER_PREFIX : "bearer " 토큰 앞 7글자 제거
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConfigEnum.AUTHORIZATION_HEADER.getGetJwtConfig());
        log.info("BEARER TOKEN VALUE : " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConfigEnum.BEARER_PREFIX.getGetJwtConfig())) {
            log.info("getJwtFromHeader Has Prefix : " + bearerToken);
            return bearerToken.substring(Integer.parseInt(JwtConfigEnum.BEARER_PREFIX_COUNT.getGetJwtConfig()));
        }
        log.info("getJwtFromHeader No Prefix : " + bearerToken);
        return bearerToken;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error(JwtErrorMessage.Invalid.getGetJwtErrorMessage());
        } catch (ExpiredJwtException | UnsupportedJwtException e) {
            log.error(JwtErrorMessage.ExpiredAndUnsupported.getGetJwtErrorMessage());
        } catch (IllegalArgumentException e) {
            log.error(JwtErrorMessage.Empty.getGetJwtErrorMessage());
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}