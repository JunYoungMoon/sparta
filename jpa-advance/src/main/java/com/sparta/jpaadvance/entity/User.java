package com.sparta.jpaadvance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Food> foodList = new ArrayList<>();

    public void addFoodList(Food food) {
        this.foodList.add(food);
        food.setUser(this);
    }

///////////////영속성 전이 저장, 삭제 방식/////////////////

//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Food> foodList = new ArrayList<>();
//
//    public void addFoodList(Food food) {
//        this.foodList.add(food);
//        food.setUser(this);
//    }

///////////////ManyToMany 중간 테이블(Order) 직접 생성 방식/////////////////

//    //User를 통해서 Food를 조회할일이 없다면 아래의 코드는 굳이 필요가 없다.
//    @OneToMany(mappedBy = "user")
//    private List<Order> orderList = new ArrayList<>();

///////////////////ManyToMany 중간 테이블 자동 생성 방식////////////////////

//    //양방향 설정
//    @ManyToMany(mappedBy = "userList")
//    private List<Food> foodList = new ArrayList<>();
//
//    public void addFoodList(Food food) {
//        this.foodList.add(food);
//        food.getUserList().add(this); // 외래 키(연관 관계) 설정
//    }

/////////////////////////////OneToMany/////////////////////////////////

//    //OneToMany의 양방향 연결인 ManyToOne은 애초에 mappedBy를 지원하지 않아
//    //아래와 같이 조회시 insertable, updatable를 false로 주고 사용해야 한다.
//    @ManyToOne
//    @JoinColumn(name = "food_id", insertable = false, updatable = false)
//    private Food food;

/////////////////////////////ManyToOne/////////////////////////////////

//    //Food ManyToOne에 대한 양방향 OneToMany 방식
//    @OneToMany(mappedBy = "user")
//    private List<Food> foodList = new ArrayList<>();
//
//    public void addFoodList(Food food) {
//        this.foodList.add(food);
//        food.setUser(this); // 외래 키(연관 관계) 설정
//    }

//    //상대쪽 setter가 없을때 상대쪽에 넣는 방법 반대쪽 메서드 추가 필요
//    public void addFoodList(Food food) {
//        this.foodList.add(food);
//        food.addUser(this); // 외래 키(연관 관계) 설정
//    }

/////////////////////////////OneToOne/////////////////////////////////

//    //OneToOne방식
//    @OneToOne(mappedBy = "user")
//    private Food food;

//    //상대쪽 setter가 있을때
//    public void addFood(Food food) {
//        this.food = food;
//        food.setUser(this);
//    }
//
//    //상대쪽 setter가 없을때 상대쪽에 넣는 방법 반대쪽 메서드 추가 필요
//    public void addFood(Food food) {
//        this.food = food;
//        this.food.addUser(this);
//    }
}