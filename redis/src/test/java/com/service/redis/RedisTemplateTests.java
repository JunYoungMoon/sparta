package com.service.redis;

import com.service.redis.dto.ItemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTests {

    // StringRedisTemplate의 String은 redis의 문자열 타입과는 다르다.
    // 정확히는 redis의 key와 value가 자바로 오면 뭐가 될것인지(여기서는 둘다 String)를 의미한다.
    // StringRedisTemplate는 redis의 문자열 타입만 작업 가능한것이 아니기 때문에 아래처럼 문자열,집합등 다 가능하다.
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void stringOpsTest() {
        // 문자열 조작을 위한 클래스
        ValueOperations<String, String> ops
                // 지금 RedisTemplate에 설정된 타입(RedisTemplate<String, String>)을 바탕으로
                // Redis 문자열 조작을 할거다.
                = stringRedisTemplate.opsForValue();

        ops.set("simpleKey", "simpleValue");
        ops.set("simpleKey", "simpleValue");
        System.out.println(ops.get("simpleKey"));

        // 집합을 조작하기 위한 클래스
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();

        setOps.add("hobbies", "games");
        setOps.add("hobbies", "coding", "alcohol", "games");

        System.out.println(setOps.size("hobbies"));

        stringRedisTemplate.expire("hobbies", 10, TimeUnit.SECONDS);
        stringRedisTemplate.delete("simpleKey");
    }

    @Autowired
    private RedisTemplate<String, ItemDto> itemDtoRedisTemplate;

    @Test
    public void itemRedisTemplateTests(){
        ///ValueOperations///
        ValueOperations<String, ItemDto> ops = itemDtoRedisTemplate.opsForValue();

        // Key-Value 저장 (TTL 10초 설정)
        ops.set("my:headset", ItemDto.builder()
                .name("Wireless Headset")
                .price(300000)
                .description("Amazing Sound")
                .build(), Duration.ofSeconds(10));

        System.out.println(ops.get("my:headset")); // 10초 후에는 null 반환

        ///HashOperations///
        HashOperations<String, String, ItemDto> hashOps = itemDtoRedisTemplate.opsForHash();

        // 여러 필드 한 번에 저장
        Map<String, ItemDto> items = new HashMap<>();
        items.put("Item1", ItemDto.builder().name("Laptop").price(2000000).description("Gaming").build());
        items.put("Item2", ItemDto.builder().name("Monitor").price(400000).description("4K Display").build());

        hashOps.putAll("hashItems", items);

        // 특정 필드 값 조회
        System.out.println(hashOps.get("hashItems", "Item1"));

        // 모든 필드 조회
        Map<String, ItemDto> allItems = hashOps.entries("hashItems");
        System.out.println(allItems);

        ///ListOperations///
        ListOperations<String, ItemDto> listOps = itemDtoRedisTemplate.opsForList();

        // 리스트에 데이터 추가
        listOps.rightPush("list:items", ItemDto.builder().name("SSD").price(120000).description("1TB Storage").build());
        listOps.rightPush("list:items", ItemDto.builder().name("RAM").price(80000).description("32GB DDR5").build());

        // 리스트 길이 확인
        System.out.println("List Size: " + listOps.size("list:items"));

        // 첫 번째 요소 조회
        System.out.println(listOps.leftPop("list:items")); // SSD 정보 출력

        // 나머지 리스트 출력
        System.out.println(listOps.range("list:items", 0, -1));

        ///SetOperations///
        SetOperations<String, ItemDto> setOps = itemDtoRedisTemplate.opsForSet();

        // Set에 데이터 추가
        setOps.add("set:products",
                ItemDto.builder().name("Tablet").price(600000).description("OLED Display").build(),
                ItemDto.builder().name("Smartwatch").price(250000).description("Heart Rate Monitor").build());

        // Set 크기 확인
        System.out.println("Set Size: " + setOps.size("set:products"));

        // 모든 데이터 조회
        System.out.println(setOps.members("set:products"));

        ///ZSetOperations///
        ZSetOperations<String, ItemDto> zSetOps = itemDtoRedisTemplate.opsForZSet();

        // 정렬된 Set에 데이터 추가 (우선순위 부여)
        zSetOps.add("zset:products", ItemDto.builder().name("GPU").price(1200000).description("RTX 4090").build(), 1);
        zSetOps.add("zset:products", ItemDto.builder().name("CPU").price(500000).description("Ryzen 9").build(), 2);

        // 범위 조회 (낮은 점수 순으로 2개 조회)
        System.out.println(zSetOps.range("zset:products", 0, 1));

        // 점수(우선순위) 확인
        System.out.println("CPU Score: " + zSetOps.score("zset:products", ItemDto.builder().name("CPU").build()));
    }
}
