package com.service.redis.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {
    private Long id;
    private Long itemId;
    private Integer count;

    public static OrderDto fromEntity(OrderDto entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .count(entity.getCount())
                .build();
    }
}
