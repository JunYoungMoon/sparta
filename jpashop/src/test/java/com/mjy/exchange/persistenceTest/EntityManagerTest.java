package com.mjy.exchange.persistenceTest;

import com.mjy.exchange.config.QuerydslConfig;
import com.mjy.exchange.dto.MemberDTO;
import com.mjy.exchange.entity.Member;
import com.mjy.exchange.queryDSL.MemberRepositoryCustomImpl;
import com.mjy.exchange.status.MemberStatus;
import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QuerydslConfig.class)
public class EntityManagerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberRepositoryCustomImpl memberRepositoryCustomImpl;

    @Test
    void testEntityManager() {

        EntityManagerFactory emf = entityManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
            TestEntity2 testEntity2 = new TestEntity2();
            testEntity2.setName("test2");
            em.persist(testEntity2);

            TestEntity testEntity = new TestEntity();
            testEntity.setName("test");
            testEntity.setTestEntity2(testEntity2);

            em.persist(testEntity);

            em.flush();
            em.clear();

//            TestEntity testEntity1 = em.find(TestEntity.class, testEntity.getId());
            //이시점에서 영속성 컨텍스트에 값이 없다면, 영속성 컨텍스트에서 DB 조회를 요청한뒤 엔티티를 생성하고 프록시 객체의 target에 실제 엔티티를 매핑한다.
            //뭔가 호출하는 시점에 값을 가져오는 느낌이든다.
//            TestEntity testEntity1 = em.getReference(TestEntity.class, testEntity.getId());

//            TestEntity test = em.find(TestEntity.class, testEntity.getId());
//            System.out.println("=============================");
//            System.out.println(test.getTestEntity2().getClass());
//            System.out.println(test.getTestEntity2().getName());    //LazyLoading이면 값을 딱 터치하는 순간(getName) 초기화
//            System.out.println("=============================");

            List<TestEntity> testEntityList = em.createQuery("select t from TestEntity t join fetch t.testEntity2 ", TestEntity.class).getResultList();

            for(TestEntity entity : testEntityList){
                System.out.println("=============================");
                System.out.println(entity.getTestEntity2().getName());
                System.out.println("=============================");
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testMemberManager() throws Exception {
        EntityManagerFactory emf = entityManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {

//            Address address = new Address("Seoul", "Gangnam-gu", "12345");
            Member member1 = Member.builder()
                    .username("john_doe")
                    .email("john.doe@example.com")
                    .password("password123")
                    .phone("010-1234-5678")
                    .memberStatus(MemberStatus.ACTIVE)
                    .build();

//            member1.setHomeAddress(address);

            em.persist(member1);

            Member member2 = Member.builder()
                    .username("john_doe")
                    .email("john.doe@example.com")
                    .password("password123")
                    .phone("010-1234-5678")
                    .memberStatus(MemberStatus.ACTIVE)
                    .build();

//            member2.setHomeAddress(address);

            em.persist(member2);

            //setter가 있을경우 같은 클래스를 보기 때문에 값이 같이 바뀐다. 불변객체인 builder를 사용하자 builder는 내부적으로 객체를 아예새로 생성
//            member1.getHomeAddress().setCity("aaa");

            // Period 값 업데이트
            member1.updateUsePeriod(
                    LocalDateTime.of(2025, 1, 1, 9, 0),
                    LocalDateTime.of(2025, 12, 31, 18, 0)
            );

            // Address 값 업데이트
            member1.updateHomeAddress("Seoul", "Gangnam-gu", "12345");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Test
    void testParentManager() {
        EntityManagerFactory emf = entityManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent parent1 = em.find(Parent.class, parent.getId());
            parent1.getChildList().remove(0);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testJPQL() throws Exception {
        EntityManagerFactory emf = entityManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
//            List<Member> members = em.createQuery("select m from Member m where m.email like '%example%'", Member.class).getResultList();

            //반환 타입이 명확할때는 TypedQuery
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.email like '%example%'", Member.class);

            //getResultList 결과가 없으면 빈 리스트를 반환
            List<Member> members = query.getResultList();

            //getSingleResult는 값이 아예 없을때와 2개 이상일때 Exception이 터짐. Spring Data JPA에서도 이걸 사용하는데 이때 try catch로 null로 혹은 Optional로 반환하도록 처리함.
            //findBy에서 사용하지 않을까 추측
            //Member member = query.getSingleResult();

            //반환 타입(username > String)이 명확할때는 TypedQuery를 사용
            //TypedQuery<String> members3 = em.createQuery("select m.username from Member m where m.email like '%example%'", String.class);
            //반환 타입(username > String,id > Long)이 명확하지 않을때는 Query를 사용
            //Query members3 = em.createQuery("select m.username, m.id from Member m where m.email like '%example%'", Member.class);

            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id =: id", Member.class)
                    .setParameter("id", 1L);
            Member singleResult = query2.getSingleResult();

            System.out.println(singleResult.getId());

            for(Member member : members){
                System.out.println(member.getUsername());
            }

            //Select 프로젝션 : Select절에서 조회할 대상을 지정하는 것
            List<MemberDTO> query3 = em.createQuery("select new com.mjy.exchange.dto.MemberDTO(m.username, m.phone) from Member m", MemberDTO.class)
                            .getResultList();

            MemberDTO memberDTO = query3.get(0);

            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getPhone());
            
            //페이징
            List<MemberDTO> query4 = em.createQuery("select new com.mjy.exchange.dto.MemberDTO(m.username, m.phone) from Member m", MemberDTO.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("size = " + query4.size());
            for (MemberDTO dto : query4) {
                System.out.println("dto = " + dto.getUsername());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    
    @Test
    public void testQueryDSL() throws Exception {
        //when &then
        List<Member> result = memberRepositoryCustomImpl.findMembers("john", null, null);

        for (Member member : result){
            System.out.println(member.getUsername());
        }
    }
}