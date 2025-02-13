package com.mjy.exchange.persistenceTest;

import com.mjy.exchange.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Child extends BaseEntity {
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    public void setParent(Parent parent) {
        this.parent = parent;
        if (parent != null && !parent.getChildList().contains(this)) {
            parent.getChildList().add(this);
        }
    }
}
