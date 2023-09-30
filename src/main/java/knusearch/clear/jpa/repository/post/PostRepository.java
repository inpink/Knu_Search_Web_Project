package knusearch.clear.jpa.repository.post;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository { //사용 안하고 있음

    private final EntityManager em; //JPA

}
