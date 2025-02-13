package com.mjy.jpashop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)   //조인전략 (★정석)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   //단일 테이블 전략
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)   //구현 클래스마다 테이블 전략 (★쓰면안됨)
//TABLE_PER_CLASS는 구현체의 모든 ID를 ITEM_ID로 하기 때문에 Movie를 조회할때도 다른 구현체의 정보도 다 가져와야한다. union all
@DiscriminatorColumn
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categoryList = new ArrayList<>();
}
