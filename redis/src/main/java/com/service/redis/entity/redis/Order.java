package com.service.redis.entity.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("order")
public class Order {
    @Id
    private String id;
    private String item;
    private Integer count;
    private Long totalPrice;
    private String status;
}
