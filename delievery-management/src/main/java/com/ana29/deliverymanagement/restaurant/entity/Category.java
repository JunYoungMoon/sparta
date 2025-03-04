package com.ana29.deliverymanagement.restaurant.entity;

import com.ana29.deliverymanagement.global.entity.Timestamped;
import com.ana29.deliverymanagement.restaurant.dto.CategoryRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더를 통한 생성만 허용
@Builder
@Table(name = "p_category")
public class Category extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id", nullable = false)
    private UUID id;

    @Column(name = "food_type", nullable = false, length = 50, unique=true)
    private String foodType;

    @Column(name = "is_deleted",nullable = false)
    @Builder.Default
    private boolean isDeleted =false;

    public void update(CategoryRequestDto requestDto) {
        this.foodType = requestDto.getFoodType();
    }

    // 명시적으로 Setter 메서드 추가
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @OneToMany(mappedBy = "category")
    @Builder.Default
    @JsonBackReference
    private List<Restaurant> restaurantList = new ArrayList<>();

}