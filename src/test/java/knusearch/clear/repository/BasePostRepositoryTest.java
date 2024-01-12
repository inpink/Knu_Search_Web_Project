package knusearch.clear.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.repository.post.BasePostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest /*@DataJpaTest 어노테이션을 사용할 때, 테스트는 실제 데이터베이스가 아닌 임베디드 데이터베이스(예: H2, HSQL)에서 실행됩니다.
Spring Boot는 이런 설정을 자동으로 처리해주어, 테스트가 실제 운영 데이터베이스에 영향을 주지 않도록 합니다. */
public class BasePostRepositoryTest {

    @Autowired
    private BasePostRepository basePostRepository;

    @Test
    public void findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence() {
        // given
        String encryptedMenuSequence = "someEncMenuSeq";
        String encryptedMenuBoardSequence = "someEncMenuBoardSeq";
        BasePost basePost = new BasePost();
        basePost.setEncryptedMenuSequence(encryptedMenuSequence);
        basePost.setEncryptedMenuBoardSequence(encryptedMenuBoardSequence);
        basePostRepository.save(basePost);

        // when
        List<BasePost> foundPosts = basePostRepository.findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence(
                encryptedMenuSequence,
                encryptedMenuBoardSequence);

        // then
        assertThat(foundPosts).isNotEmpty();
        assertThat(foundPosts.get(0).getEncryptedMenuSequence()).isEqualTo(encryptedMenuSequence);
        assertThat(foundPosts.get(0).getEncryptedMenuBoardSequence()).isEqualTo(encryptedMenuBoardSequence);
    }
}