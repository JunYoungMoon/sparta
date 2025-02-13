package com.mjy.exchange.persistenceTest;

import com.mjy.exchange.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TestEntity2 extends BaseEntity {
    String name;
}
