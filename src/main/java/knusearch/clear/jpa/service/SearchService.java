package knusearch.clear.jpa.service;

/*
readOnly 속성은 해당 메서드에서 데이터베이스의 읽기 작업만 수행하고, 쓰기 작업은 하지 않음을 나타냅니다.
이렇게 설정된 메서드는 트랜잭션 커밋 시에 롤백되는 것을 방지하고, 데이터베이스에 대한 읽기 작업을 최적화할 수 있습니다.
 */

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import knusearch.clear.jpa.domain.dto.BasePostRequest;
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

    public List<Map.Entry<BasePostRequest, Integer>> searchAndPostWithBoostClassification(List<String> words,
                                                                                          String classification) {
        Map<BasePostRequest, Integer> postWithCount = calculateCount(words);

        Map<BasePostRequest, Integer> postWithCountAndClass = countClassificationWeight(
                postWithCount,
                classification);

        return sortPosts(postWithCountAndClass);
    }

    private Map<BasePostRequest, Integer> countClassificationWeight(
            Map<BasePostRequest, Integer> postWithCount,
            String classification) {
        Map<BasePostRequest, Integer> withClass = new HashMap<>();

        /*final int weight = postWithCount.values().stream()
                .max(Integer::compare).get() / 2;*/
        final int weight = 11;

        postWithCount.forEach((post, count) -> {
            if (classification.equals(post.classification())) {
                withClass.put(post, count * weight / 10);
            } else {
                withClass.put(post, count);
            }
        });

        return withClass;
    }

    public List<Map.Entry<BasePostRequest, Integer>> searchAndPosts(List<String> words) {
        Map<BasePostRequest, Integer> postWithCount = calculateCount(words);

        return sortPosts(postWithCount);
    }

    private Map<BasePostRequest, Integer> calculateCount(List<String> words) {
        Set<BasePostRequest> allPosts = new HashSet<>();
        for (String word : words) {
            List<BasePostRequest> posts = basePostRepository.findByTitleOrTextQuery(
                    word,
                    word);

            for (BasePostRequest post : posts) {
                allPosts.add(post);
            }
        }

        Map<BasePostRequest, Integer> postWithCount = countQueryOccurrencesInTitles(allPosts, words);
        return postWithCount;
    }

    private List<Map.Entry<BasePostRequest, Integer>> sortPosts(Map<BasePostRequest, Integer> postWithCount) {
        return postWithCount.entrySet().stream()
                .sorted(Map.Entry.<BasePostRequest, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(entry -> entry.getKey().dateTime(), Comparator.reverseOrder())
                        .thenComparing(entry -> entry.getKey().id()))
                .toList();
    }

    // RDB에는 일치하는 단어 개수 세어주는 기능 제공하지 않아서 직접 구현해야 함
    public Map<BasePostRequest, Integer> countQueryOccurrencesInTitles(Set<BasePostRequest> allPosts,
                                                                       List<String> words) {
        // 게시글 별 점수
        final Map<BasePostRequest, Integer> postCount = new HashMap<>();

        // 단어별 최소 및 최대 등장 횟수 설정
        Map<String, Integer[]> wordMinMaxCounts = new HashMap<>();

        for (String word : words) {
            wordMinMaxCounts.put(word, new Integer[]{0,0});
        }

        // 게시글별 단어 등장 횟수
        Map<BasePostRequest, Map<String, Integer>> postWordCount = new HashMap<>();

        for (BasePostRequest post : allPosts) {
            final String title = post.title();
            final String text = post.text();
            Map<String, Integer> wordCount = new HashMap<>(); // 사회 :3 , 복지 : 1 ..

            for (String word : words) { // 사회 복지 학부 졸업
                final int titleCount = (title.length() - title.replace(word, "").length()) / word.length();
                final int textCount = (text.length() - text.replace(word, "").length()) / word.length();
                final int currentCount = (titleCount + textCount);

                Integer[] minMax = wordMinMaxCounts.get(word);
                int min = Math.min(minMax[0],currentCount);
                int max = Math.max(minMax[1],currentCount);

                wordMinMaxCounts.put(word,new Integer[]{min,max});
                wordCount.put(word,currentCount);
            }
            postWordCount.put(post,wordCount);
        }

        for (Map.Entry<BasePostRequest, Map<String, Integer>> entry : postWordCount.entrySet()) {
            BasePostRequest post = entry.getKey();
            Map<String, Integer> wordCounts = entry.getValue();
            double score = calculatePostScore(wordCounts,wordMinMaxCounts);

            // 시간 가중치 계산
            long daysAgo = ChronoUnit.DAYS.between(post.dateTime(), LocalDateTime.now());
            double timeWeight = 1.0 / (Math.log(1.0 + daysAgo) + 1); // 로그 함수 사용하여 가중치 조절

            // 최종 점수에 시간 가중치 반영
            score *= timeWeight;

            postCount.put(post, Integer.valueOf((int) (score*100))); // 소수점 둘째자리까지 100곱해서 사용
        }

        System.out.println("wordMinMaxCounts = " );
        for (Map.Entry<String, Integer[]> entry : wordMinMaxCounts.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue()[0]+" "+entry.getValue()[1]);
        }

/*        System.out.println("postCount = ");
        for (Map.Entry<BasePostRequest, Integer> entry : postCount.entrySet()) {
            System.out.println(entry.getKey().title() + " " + entry.getValue());
        }*/
        return postCount;
    }

    // 정규화 점수
    private static double calculatePostScore(Map<String, Integer> postCounts,
                                             Map<String, Integer[]> wordMinMaxCounts) {
        double score = 0.0;

        for (Map.Entry<String, Integer> entry : postCounts.entrySet()) {
            String word = entry.getKey();
            Integer count = entry.getValue();
            Integer[] minMax = wordMinMaxCounts.get(word);
            if (minMax != null) {
                double normalized = normalize(count, minMax[0], minMax[1]);
                score += normalized;
            }
        }

        return score;
    }

    // 선형 정규화 공식
    private static double normalize(int value, int min, int max) {
        if (max==0) {
            return 0;
        }
        return (double) (value - min) / (max - min);
    }
}
