package com.sparta.jpaadvance.orphan;

import com.sparta.jpaadvance.entity.Food;
import com.sparta.jpaadvance.entity.User;
import com.sparta.jpaadvance.respository.FoodRepository;
import com.sparta.jpaadvance.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OrphanTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FoodRepository foodRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void init() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setName("Robbie");
        userList.add(user1);

        User user2 = new User();
        user2.setName("Robbert");
        userList.add(user2);
        userRepository.saveAll(userList);

        List<Food> foodList = new ArrayList<>();
        Food food1 = new Food();
        food1.setName("고구마 피자");
        food1.setPrice(30000);
        food1.setUser(user1); // 외래 키(연관 관계) 설정
        foodList.add(food1);

        Food food2 = new Food();
        food2.setName("아보카도 피자");
        food2.setPrice(50000);
        food2.setUser(user1); // 외래 키(연관 관계) 설정
        foodList.add(food2);

        Food food3 = new Food();
        food3.setName("후라이드 치킨");
        food3.setPrice(15000);
        food3.setUser(user1); // 외래 키(연관 관계) 설정
        foodList.add(food3);

        Food food4 = new Food();
        food4.setName("양념 치킨");
        food4.setPrice(20000);
        food4.setUser(user2); // 외래 키(연관 관계) 설정
        foodList.add(food4);

        Food food5 = new Food();
        food5.setName("고구마 피자");
        food5.setPrice(30000);
        food5.setUser(user2); // 외래 키(연관 관계) 설정
        foodList.add(food5);
        foodRepository.saveAll(foodList);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("연관관계 제거")
    void test1() {
        //연관관계 주인이 자식엔티티, 주인이 아닌곳이 부모 엔티티이다.
        //User는 부모 엔티티다. 쉽게 mappedBy를 하는곳이 부모엔티티라고 생각하면 된다.
        //Food는 자식 엔티티다. 연관관계 주인임으로 JoinColumn을 사용해서 FK가 있다.

        // 고객 Robbie 를 조회합니다.
        User user = userRepository.findByName("Robbie");
        System.out.println("user.getName() = " + user.getName());

        // 연관된 음식 Entity 제거 : 후라이드 치킨
        Food chicken = null;
        for (Food food : user.getFoodList()) {
            if(food.getName().equals("후라이드 치킨")) {
                chicken = food;
            }
        }
        if(chicken != null) {
            // orphanRemoval = true 옵션으로 "★부모-자식 관계가 끊어졌을때★"
            // 즉 자식 엔티티가 컬렉션에서 제거되거나 null로 설정되었을때 자동 삭제된다.
            // CascadeType.REMOVE 옵션과 헷갈리지 말것
            user.getFoodList().remove(chicken);
        }

        // 연관관계 제거 확인
        for (Food food : user.getFoodList()) {
            System.out.println("food.getName() = " + food.getName());
        }
    }
}
