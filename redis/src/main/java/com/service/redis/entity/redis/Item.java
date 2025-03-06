package com.service.redis.entity.redis;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Entity 대신 RedisHash
@RedisHash("item")
public class Item implements Serializable {
    @Id
    // Id String이면 자동으로 UUID가 배정된다.
    // Id Long이면 자동으로 임의의 숫자가 배정된다.
    private String id;
    private String name;
    private String description;
    private Integer price;
}
