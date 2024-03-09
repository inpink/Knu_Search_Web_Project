package knusearch.clear.service;

import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.service.SearchService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @ParameterizedTest
    @MethodSource("provideTestData")
    void countQueryOccurrencesInTitles(String query, String title1, String text1, String title2, String text2, int expectedCount1, int expectedCount2) {
        // Given
        BasePost post1 = new BasePost();
        post1.setTitle(title1);
        post1.setText(text1);

        BasePost post2 = new BasePost();
        post2.setTitle(title2);
        post2.setText(text2);

        List<BasePost> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);

        // When
        Map<BasePost, Integer> result = searchService.countQueryOccurrencesInTitles(posts, query);

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(post1)).isEqualTo(expectedCount1);
        assertThat(result.get(post2)).isEqualTo(expectedCount2);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(
                        "검색 단어",
                        "ㅎㅎ 검색 단어가 이 문장에 몇 개 있을까요? 검색단어는 띄어쓰기가 달라서 안 됩니다. 검색 단어만 됩니다.",
                        "Nope",
                        "단어 검색 단어 단어단어 ㅋㅋ 단어 단어",
                        "ㅎ단어ㅎ!!!!검색 단어ㅇㅇㅇㅇ검색 단어ㅇㅇ",
                        2, // post1에서 기대되는 검색 단어의 출현 횟수
                        3  // post2에서 기대되는 검색 단어의 출현 횟수
                ),
                Arguments.of(
                        "단어",
                        "ㅎㅎ 검색 단어가 이 문장에 몇 개 있을까요? 검색단어는 띄어쓰기가 달라서 안 됩니다. 검색 단어만 됩니다.",
                        "단어단어단어단어",
                        "단어 검색 단어 단어단어 ㅋㅋ 단어 단어",
                        "ㅎ단어ㅎ!!!!검색 단어ㅇㅇㅇㅇ검색 단어ㅇㅇ",
                        7, // post1에서 기대되는 검색 단어의 출현 횟수
                        9  // post2에서 기대되는 검색 단어의 출현 횟수
                )

        );
    }

}
