package knusearch.clear.service;

import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void countQueryOccurrencesInTitles() {
        // Given
        String query = "검색 단어";
        BasePost post1 = new BasePost();
        post1.setTitle("ㅎㅎ 검색 단어가 이 문장에 몇 개 있을까요? 검색단어는 띄어쓰기가 달라서 안 됩니다. 검색 단어만 됩니다.");
        post1.setText("Nope");

        BasePost post2 = new BasePost();
        post2.setTitle("단어 검색 단어 단어단어 ㅋㅋ 단어 단어");
        post2.setText("ㅎ단어ㅎ!!!!검색 단어ㅇㅇㅇㅇ검색 단어ㅇㅇ");

        List<BasePost> posts = new ArrayList<>() {{
            add(post1);
            add(post2);
        }};

        // When
        Map<BasePost, Integer> result = searchService.countQueryOccurrencesInTitles(posts, query);

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(post1)).isEqualTo(2);
        assertThat(result.get(post2)).isEqualTo(3);
    }

}
