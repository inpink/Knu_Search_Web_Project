package knusearch.clear.elasticsearch.repository;

import java.util.List;
import knusearch.clear.elasticsearch.domain.BasePostElasticsearchEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BasePostElasticsearchRepository
        extends ElasticsearchRepository<BasePostElasticsearchEntity, String> {

    List<BasePostElasticsearchEntity> findByTitleOrText(String title, String text);

    //basepost-index의  Nori 형태소 분석기를 사용하는 옵션에서,"nori_part_of_speech" 필터를 사용하여 명사만 추출할 수 있도록 설정했다.
    //이제 장, ㄴ 같은 명사가 아닌 단어는 검색되지 않는다.
    // "장애인 모집"으로 테스트했을때 검색 결과 매우 좋았다. "장애" "장애인"이 많이 들어간 단어가 높게 평가됨.
    // "전시회 접수" 또한 마찬가지이다. 강남대 홈피랑 비교했을때 매우매우 만족스러움^_^
    // "사회ㅣ" 이런 오타도 알아서 걸러줌! 명사만 하게 했으니까~

    // logstash설정에서 statement => "SELECT * FROM post_main UNION ALL SELECT * FROM post_ict"를 통해,
    //basepost-index에 모든 post들을 몰아줬다. 추후 post구분번호를 BasePost에 추가해주고, 검색 시 @Query할 수 있게 하자!
    //어차피 대부분의 경우는 통합검색을 할거고, post_main이 제일 양이 방대하기때문에
    // 매번 union하는것보단 한번에 해두고 뺄거(비교적 일부)만 빼는 것이 이득일 것이라 판단됐기 때문!
    // inverted index 자료구조를 사용하기에, es는 검색속도도 매우 빠르다.
    // 여러가지 검색해봤는데.. score 알고리즘이 잘되어있어서 사용자 정의 사전은 굳이 필요없을듯...

    //TODO: 엘라스틱서치의 score 계산법
    // : https://kazaana2009.tistory.com/6
    // : https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-function-score-query.html
    // : elasticsearch score 검색 등 해서 참고해서 더 공부하기!
    //"사회복지 졸업"이라고 검색하면, 기본기능인 위의 findByTitleOrText는
    // "사회복지"와 "졸업" 둘 다가 있어야만 검색이 돼
    //아래 메소드는 @Query를 이용하여 "사회복지"와 "졸업"중 하나라도 단어가 있으면 출력됨.
    //(\"should\" 절은 OR 연산자 역할을 함. 따라서 "title" 필드 또는 "text" 필드 중 하나라도
    // "사회복지" 또는 "졸업"이라는 단어를 포함하고 있는 문서가 검색 결과에 포함됨)
    // "사회복지"와 "졸업"은 nori 형태소 분석기에 의해 분리되니 분리걱정은 하지않아도 됨.
    //정렬 기준도, es가 애초에 score 알고리즘을 쓰므로 걱정하지 않아도 됨.
    @Query("{\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"text\": \"?0\"}}]}}")
    List<BasePostElasticsearchEntity> searchByTitleOrText(String title, String text);


    @Query("{\n" +
            "  \"function_score\": {\n" +
            "    \"query\": {\n" +
            "      \"bool\": {\n" +
            "        \"should\": [\n" +
            "          {\"match\": {\"title\": \"?0\"}},\n" +
            "          {\"match\": {\"text\": \"?0\"}}\n" +
            "        ]\n" +
            "      }\n" +
            "    },\n" +
            "    \"functions\": [\n" +
            "      {\n" +
            "        \"filter\": {\"term\": {\"classification\": \"?1\"}},\n" +
            "        \"weight\": 1.5\n" + // 같은 분류일 때 줄 점수 가중치
            "      }\n" +
            "    ],\n" +
            "    \"boost_mode\": \"multiply\"\n" +
            "  }\n" +
            "}")
    List<BasePostElasticsearchEntity> searchByTitleTextAndBoostClassification(String title,
                                                                              String text,
                                                                              String classification);

}
