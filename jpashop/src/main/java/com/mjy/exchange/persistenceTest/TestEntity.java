package com.mjy.exchange.persistenceTest;

import com.mjy.exchange.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TestEntity extends BaseEntity {
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEST_ENTITY2_ID")
    private TestEntity2 testEntity2;
}
