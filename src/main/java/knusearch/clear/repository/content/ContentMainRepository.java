package knusearch.clear.repository.content;

import jakarta.persistence.EntityManager;
import knusearch.clear.domain.content.BaseContent;
import knusearch.clear.domain.content.ContentMain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentMainRepository {

    private final EntityManager em;

    public void save(BaseContent contentMain) {
        em.persist(contentMain);
    }

    //
    public ContentMain findOne(Long id) {
        return em.find(ContentMain.class, id);
    }

    public List<ContentMain> findAllByEnc(String encMenuSeq, String encMenuBoardSeq){
        return em.createQuery(
                        "SELECT cm FROM ContentMain cm " +
                                "WHERE cm.encMenuSeq = :encMenuSeq AND cm.encMenuBoardSeq = :encMenuBoardSeq", ContentMain.class)
                .setParameter("encMenuSeq", encMenuSeq)
                .setParameter("encMenuBoardSeq", encMenuBoardSeq)
                .getResultList();
    }

    public List<ContentMain> findAll() {
        return em.createQuery("select m from ContentMain m", ContentMain.class)
                .getResultList(); //RDB에서 쓰는 방식. 엘라스틱 서치는 다른 방식 써줘야 함
    }



}
