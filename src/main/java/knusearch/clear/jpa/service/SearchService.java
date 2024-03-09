package knusearch.clear.jpa.service;

/*
readOnly 속성은 해당 메서드에서 데이터베이스의 읽기 작업만 수행하고, 쓰기 작업은 하지 않음을 나타냅니다.
이렇게 설정된 메서드는 트랜잭션 커밋 시에 롤백되는 것을 방지하고, 데이터베이스에 대한 읽기 작업을 최적화할 수 있습니다.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import knusearch.clear.jpa.domain.dto.BasePostMapper;
import knusearch.clear.jpa.domain.dto.SearchResult;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.repository.SearchRepository;
import knusearch.clear.jpa.repository.post.BasePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final BasePostRepository basePostRepository;

    //나중에는 repository(DAO)에서 가져올 듯
    public String findOrder() {
        return "InOrderChecked";
    }

    public String findPeriod() {
        return "allTimeChecked";
    }

    public List<String> findSites() {
        List<String> sites = new ArrayList<>() {{
            add("unifiedSearchChecked");
            add("knuMainSiteChecked");
            add("knuIctSiteChecked");
            add("knuWelfareSiteChecked");
            add("knuSeniorSiteChecked");
            add("knuArtSiteChecked");
            add("knuClasSiteChecked");
            add("knuCtlSiteChecked");
        }};

        return sites;
    }

    public List<SearchResult> searchAndPostWithBoostClassification(String searchQuery, String refinedPredictedClass) {

    }

    public List<SearchResult> searchAndPosts(String searchQuery) {
        List<BasePost> allPosts = basePostRepository.findByTitleOrTextQuery(searchQuery, searchQuery);
        Map<BasePost, Integer> postWithCount = countQueryOccurrencesInTitles(allPosts, searchQuery);

        

        List<SearchResult> searchResults = BasePostMapper.toSearchResult();
        return searchResults;
    }

    // RDB에는 일치하는 단어 개수 세어주는 기능 제공하지 않아서 직접 구현해야 함
    public Map<BasePost, Integer> countQueryOccurrencesInTitles(List<BasePost> allPosts, String searchQuery) {
        final Map<BasePost, Integer> postWithCount = new HashMap<>();
        for (BasePost post : allPosts) {
            final String title = post.getTitle();
            final String text = post.getText();
            final int titleCount = (title.length() - title.replace(searchQuery, "").length()) / searchQuery.length();
            final int textCount = (text.length() - text.replace(searchQuery, "").length()) / searchQuery.length();
            final int totalCount = titleCount + textCount;

            postWithCount.put(post, totalCount);
        }

        return postWithCount;
    }
}
