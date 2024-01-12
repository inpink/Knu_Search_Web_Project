package knusearch.clear.jpa.repository.post;

import java.util.List;
import knusearch.clear.jpa.domain.post.BasePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasePostRepository extends JpaRepository<BasePost, Long> {

    List<BasePost> findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence(
            String EncryptedMenuSequence,
            String EncryptedMenuBoardSequence);
}
