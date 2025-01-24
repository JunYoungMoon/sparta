package com.sparta.jpaadvance.respository;

import com.sparta.jpaadvance.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
