package com.mjy.exchange.persistenceTest;

import com.mjy.exchange.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Parent extends BaseEntity {
    String name;

    //완전히 Parent와 Child가 완전히 종속적일때만 cascade를 사용한다.다른 엔티티에서 Child를 보고 있다면 Parent에서 제거시에도 같이 제거된다.
    //orphanRemoval는 고아객체로 부모의 리스트 컬렉션에서 remove가 일어났을때 child의 테이블에서 해당 child를 delete한다.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        if (!childList.contains(child)) {
            childList.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(Child child) {
        if (childList.remove(child)) {
            child.setParent(null);
        }
    }
}
