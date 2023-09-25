package knusearch.clear.repository.content;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContentRepository {

    private final EntityManager em; //JPA

}
