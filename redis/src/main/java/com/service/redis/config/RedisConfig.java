package com.service.redis.config;

import com.service.redis.dto.ItemDto;
import com.service.redis.dto.OrderDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ItemDto> itemRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ItemDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        //키를 redis에 넣어주는 과정에서 어떤 직렬화기를 쓸것인가 string
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> articleScoreRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, String> articleUserRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> articleRankRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, String> cartTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OrderDto> orderTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, OrderDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return redisTemplate;
    }

    // RedisSerializer.json()를 사용하면 세션 정보를 JSON 형태로 쉽게 읽을 수 있지만
    // UsernamePasswordAuthenticationToken는 기본 생성자가 없다. 하지만 Jackson은 역직렬화할 때 기본 생성자가 없으면 객체를 생성할 수 없다.
    // 그래서 Jackson에서 생성자를 인식하려면 @JsonCreator 애너테이션을 사용하여 객체 생성 방법을 명시해줘야 한다.

    // SecurityContextImpl도 마찬가지로 기본 생성자가 없으며, 내부적으로 UsernamePasswordAuthenticationToken을 포함하고 있다.
    // 따라서 SecurityContextImpl을 JSON으로 저장하려고 하면, 내부의 UsernamePasswordAuthenticationToken이 역직렬화되지 않아 에러가 발생할 수 있다.
    // 이를 해결하려면 SecurityContextImpl과 UsernamePasswordAuthenticationToken을 Jackson이 직렬화/역직렬화할 수 있도록 커스터마이징해야 한다.

    // json은 아래와 같이 생긴걸 볼수 있는데, SecurityContextImpl를 통으로 직렬화 해서 들어간다. 그래서 역직렬화가 필요하다.
    // jwt의 accessToken은 굳이 이렇게 통으로 다 넣을 필요도 없고 refresh토큰도 이렇게 넣으면 데이터가 너무 많긴하다.
    //{
    //  "@class": "org.springframework.security.core.context.SecurityContextImpl",
    //  "authentication": {
    //    "@class": "org.springframework.security.authentication.UsernamePasswordAuthenticationToken",
    //    "authorities": [
    //      "java.util.Collections$UnmodifiableRandomAccessList",
    //      []
    //    ],
    //    "details": {
    //      "@class": "org.springframework.security.web.authentication.WebAuthenticationDetails",
    //      "remoteAddress": "0:0:0:0:0:0:0:1",
    //      "sessionId": null
    //    },
    //    "authenticated": true,
    //    "principal": {
    //      "@class": "org.springframework.security.core.userdetails.User",
    //      "password": null,
    //      "username": "user1",
    //      "authorities": [
    //        "java.util.Collections$UnmodifiableSet",
    //        []
    //      ],
    //      "accountNonExpired": true,
    //      "accountNonLocked": true,
    //      "credentialsNonExpired": true,
    //      "enabled": true
    //    },
    //    "credentials": null,
    //    "name": "user1"
    //  }
    //}
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        return RedisSerializer.json();
        return RedisSerializer.java();
    }
}
