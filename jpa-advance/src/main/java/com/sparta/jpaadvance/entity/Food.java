package com.sparta.jpaadvance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "food")

public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

///////////////ManyToMany 중간 테이블(Order) 직접 생성 방식/////////////////

//    //Food를 통해서 User를 조회할일이 없다면 아래의 코드는 굳이 필요가 없다.
//    @OneToMany(mappedBy = "food")
//    private List<Order> orderList = new ArrayList<>();

///////////////////ManyToMany 중간 테이블 자동 생성 방식////////////////////

//    //중간 테이블이 없다면 중간테이블을 자동으로 생성하고 중간 테이블은 pk도 없고 테이블 변경시 관리하기 힘들다.
//    @ManyToMany
//    @JoinTable(name = "orders", // 중간 테이블 생성
//            joinColumns = @JoinColumn(name = "food_id"), // 현재 위치인 Food Entity 에서 중간 테이블로 조인할 컬럼 설정
//            inverseJoinColumns = @JoinColumn(name = "user_id")) // 반대 위치인 User Entity 에서 중간 테이블로 조인할 컬럼 설정
//    private List<User> userList = new ArrayList<>();
//
//    //양방향 설정
//    public void addUserList(User user) {
//        this.userList.add(user); // 외래 키(연관 관계) 설정
//        user.getFoodList().add(this);
//    }

/////////////////////////////OneToMany/////////////////////////////////

//    //JoinColumn이 여기 있어서 연관관계의 주인은 Food이지만 Many가 User 쪽이기 때문에 User에서 food_id(FK)가 생성된다.
//    //User, Food가 각각 insert되고 여기에 있는 userList의 값이 있다면 User 테이블의 food_id(FK)를 업데이트한다.
//    @OneToMany
//    @JoinColumn(name = "food_id") // users 테이블에 food_id 컬럼
//    private List<User> userList = new ArrayList<>();

/////////////////////////////ManyToOne/////////////////////////////////

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    Food가 setter가 없을때 상대쪽에서 넣어주는 방법
//    public void addUser(User user) {
//        this.user = user;
//    }

/////////////////////////////OneToOne/////////////////////////////////

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
////    Food가 setter가 없을때 상대쪽에서 넣어주는 방법
//    public void addUser(User user) {
//        this.user = user;
//    }
}