package com.service.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory
    ) {
        // 설정 구성을 먼저 진행한다.
        // Redis를 이용해서 Spring Cache를 사용할 때
        // Redis 관련 설정을 모아두는 클래스
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                // null을 캐싱하는지
                .disableCachingNullValues()
                // 기본 캐시 유지 시간 (Time To Live)
                .entryTtl(Duration.ofSeconds(120))
                // 캐시를 구분하는 접두사 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할것인지
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
                );

        RedisCacheConfiguration individual = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(20))
                //20초안에 다시 사용시 또 만료 기간이 늘어나도록 한다.
                .enableTimeToIdle()
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .withCacheConfiguration("itemCache", individual)
                .build();
    }
}
