package knusearch.clear.jpa.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import knusearch.clear.jpa.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository { //DAO(Data Access Object) : 실제로 DB에 접근.
    // https://haem-jsp.tistory.com/57 <- 여기 정리 잘 돼있음

    private final EntityManager em; //JPA

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}

//DAO는 Data Access Object의 약자로, 실제로 DB에 접근하는 객체. JPA에서는 Repository가 DAO
//https://velog.io/@ohzzi/Entity-DAO-DTO%EA%B0%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EB%A9%B0-%EC%99%9C-%EC%82%AC%EC%9A%A9%ED%95%A0%EA%B9%8C
//https://okky.kr/questions/998548 참고해보기
//결론은 Entity, DAO 둘 다 필요하고 DTO도 쓰면 좋다