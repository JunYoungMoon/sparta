package com.mjy.jpashop2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Team team){
        em.persist(team);
        return team.getId();
    }
}
