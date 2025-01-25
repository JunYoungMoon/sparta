package com.sparta.jpaadvance.fetch;

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
public class FetchTypeTest {

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
        food4.setName("후라이드 치킨");
        food4.setPrice(15000);
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
    @DisplayName("아보카도 피자 조회")
    void test1() {
        Food food = foodRepository.findById(2L).orElseThrow(NullPointerException::new);

        System.out.println("food.getName() = " + food.getName());
        System.out.println("food.getPrice() = " + food.getPrice());

        System.out.println("아보카도 피자를 주문한 회원 정보 조회");
        System.out.println("food.getUser().getName() = " + food.getUser().getName());
    }

    @Test
    @Transactional
    @DisplayName("Robbie 고객 조회")
    void test2() {
        User user = userRepository.findByName("Robbie");
        System.out.println("user.getName() = " + user.getName());

        System.out.println("Robbie가 주문한 음식 이름 조회");
        for (Food food : user.getFoodList()) {
            System.out.println(food.getName());
        }
    }

    @Test
    @DisplayName("Robbie 고객 조회 실패")
    void test3() {

        User user = userRepository.findByName("Robbie");
        System.out.println("user.getName() = " + user.getName());

        System.out.println("Robbie가 주문한 음식 이름 조회");
        //User 엔티티에서 foodList는 OneToMany로 뒤에가 Many니깐 디폴트 값이 Lazy 옵션이다.
        //그래서 필요할때 Many에 해당하는 값들을 가져오는데 이때 프록시 객체를 생성한다.
        //즉 프록시 객체는 One에 해당하는것을 영속성 컨텍스트를 먼저 조회하여 가져오고 (없으면 DB접근)
        //Many에 해당하는것은 메타 데이터만 가지고 있다 그리고 메타 데이터를 호출시 데이터를 가져온다.
        for (Food food : user.getFoodList()) {
            System.out.println(food.getName());
        }
    }
}