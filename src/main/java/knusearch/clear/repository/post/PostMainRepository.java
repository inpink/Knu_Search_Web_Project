package knusearch.clear.repository.post;

import jakarta.persistence.EntityManager;
import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostMain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostMainRepository implements BasePostRepository<PostMain> {

    private final EntityManager em;

    @Override
    public void save(BasePost postMain) {
        em.persist(postMain);
    }

    @Override
    public PostMain findOne(Long id) {
        return em.find(PostMain.class, id);
    }

    @Override
    public List<PostMain> findAllByEnc(String encMenuSeq, String encMenuBoardSeq){
        return em.createQuery(
                        "SELECT cm FROM PostMain cm " +
                                "WHERE cm.encMenuSeq = :encMenuSeq AND cm.encMenuBoardSeq = :encMenuBoardSeq", PostMain.class)
                .setParameter("encMenuSeq", encMenuSeq)
                .setParameter("encMenuBoardSeq", encMenuBoardSeq)
                .getResultList();
    }

    @Override
    public List<PostMain> findAll() {
        return em.createQuery("select m from PostMain m", PostMain.class)
                .getResultList(); //RDB에서 쓰는 방식. 엘라스틱 서치는 다른 방식 써줘야 함
    }


}
