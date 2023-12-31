package knusearch.clear.jpa.repository.post;

import jakarta.persistence.EntityManager;
import java.util.List;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.domain.post.PostIct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostIctRepository implements BasePostRepository<PostIct> {

    private final EntityManager em;

    @Override
    public void save(BasePost postIct) {
        em.persist(postIct);
    }

    @Override
    public PostIct findOne(Long id) {
        return em.find(PostIct.class, id);
    }

    @Override
    public List<PostIct> findAllByEnc(String encMenuSeq, String encMenuBoardSeq) {
        return em.createQuery(
                        "SELECT cm FROM PostIct cm " +
                                "WHERE cm.encMenuSeq = :encMenuSeq AND cm.encMenuBoardSeq = :encMenuBoardSeq", PostIct.class)
                .setParameter("encMenuSeq", encMenuSeq)
                .setParameter("encMenuBoardSeq", encMenuBoardSeq)
                .getResultList();
    }

    @Override
    public List<PostIct> findAll() {
        return em.createQuery("select m from PostIct m", PostIct.class)
                .getResultList(); //RDB에서 쓰는 방식. 엘라스틱 서치는 다른 방식 써줘야 함
    }
}
