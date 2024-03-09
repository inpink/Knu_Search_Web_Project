package knusearch.clear.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import knusearch.clear.jpa.domain.dto.BasePostRequest;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.repository.post.BasePostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.ApplicationContext;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //DataSource가 바뀜
@DataJpaTest /*@DataJpaTest 어노테이션을 사용할 때, 테스트는 실제 데이터베이스가 아닌 임베디드 데이터베이스(예: H2, HSQL)에서 실행됩니다.
Spring Boot는 이런 설정을 자동으로 처리해주어, 테스트가 실제 운영 데이터베이스에 영향을 주지 않도록 합니다. */
@TestPropertySource("classpath:application-test.properties")
public class BasePostRepositoryTest {

    @Autowired
    private BasePostRepository basePostRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @Test
    public void findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence() throws SQLException {
        String ddlAuto = context.getEnvironment().getProperty("spring.jpa.hibernate.ddl-auto");
        System.out.println("ddl-auto: " + ddlAuto); // ApplicationContext는 내가 사용하는 application-test.properties의 값이 나옴!!
        System.out.println(context.getEnvironment().getProperty("spring.datasource.username"));

        System.out.println(dataSource.toString());
        System.out.println(dataSource.getConnection().getMetaData());
        //application-test.properties 이용하면 : org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory$EmbeddedDataSourceProxy@4b4bc73d
        //이용 안하고 default이용하면 : HikariDataSource (HikariPool-1) => MySQL 사용하고 있음
        // => DataSource에 담기는 값이 달라지는게 맞다. 디버깅해도 그럼.

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

    @Test
    public void findByTitleOrTextQuery() {
        // Given
        String title = "Sample Title";
        String text = "Sample text content";
        BasePost post = new BasePost();
        post.setTitle(title);
        post.setText(text);
        basePostRepository.save(post);

        // When
        List<BasePostRequest> resultByTitle = basePostRepository.findByTitleOrTextQuery(title, "irrelevantTextQuery");
        List<BasePostRequest> resultByText = basePostRepository.findByTitleOrTextQuery("irrelevantTitleQuery", text);

        // Then
        assertThat(resultByTitle).isNotEmpty();
        assertThat(resultByTitle.get(0).title()).isEqualTo(title);

        assertThat(resultByText).isNotEmpty();
        assertThat(resultByText.get(0).text()).isEqualTo(text);
    }
}