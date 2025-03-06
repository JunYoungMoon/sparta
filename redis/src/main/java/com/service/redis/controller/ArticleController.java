package com.service.redis.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final RedisTemplate<String, Long> articleRankRedisTemplate;
    private final RedisTemplate<String, String> articleUserRedisTemplate;

    @Autowired
    public ArticleController(
            RedisTemplate<String, Long> articleRankRedisTemplate,
            RedisTemplate<String, String> articleUserRedisTemplate
    ) {
        this.articleRankRedisTemplate = articleRankRedisTemplate;
        this.articleUserRedisTemplate = articleUserRedisTemplate;
    }

    // 게시글 점수 증가 (조회수 증가)
    @PostMapping("/{articleId}/increment")
    public ResponseEntity<Long> incrementArticleScore(@PathVariable Long articleId) {
        String redisKey = "article:score:" + articleId;

        // 조회수 증가
        Long increment = articleRankRedisTemplate.opsForValue().increment(redisKey, 1);

        return ResponseEntity.ok(increment);
    }

    // 게시글에 유저 추가
    @PostMapping("/{articleId}/addUser/{username}")
    public ResponseEntity<Long> addUserToArticle(@PathVariable Long articleId, @PathVariable String username) {
        String redisKey = "article:users:" + articleId;

        // SADD 연산 수행
        Long added = articleUserRedisTemplate.opsForSet().add(redisKey, username);

        // 성공적으로 추가된 경우만 인기 점수 증가
        if (added != null && added > 0) {
            articleUserRedisTemplate.opsForZSet().incrementScore("article:ranks", redisKey, 1);
        }

        return ResponseEntity.ok(added);
    }

    // 특정 게시글의 유저 수 조회
    @GetMapping("/{articleId}/userCount")
    public ResponseEntity<Long> getArticleUserCount(@PathVariable Long articleId) {
        String redisKey = "article:users:" + articleId;

        // SCARD (유저 수 확인)
        Long count = articleUserRedisTemplate.opsForSet().size(redisKey);

        return ResponseEntity.ok(count);
    }

    // 가장 인기 있는 게시글 가져오기
    @GetMapping("/top")
    public ResponseEntity<Set<String>> getTopArticles(@RequestParam(defaultValue = "1") int count) {
        // ZRANGE (가장 높은 순위의 게시글 가져오기)
        Set<String> topArticles = articleUserRedisTemplate.opsForZSet()
                .reverseRange("article:ranks", 0, count - 1);

        return ResponseEntity.ok(topArticles);
    }
}