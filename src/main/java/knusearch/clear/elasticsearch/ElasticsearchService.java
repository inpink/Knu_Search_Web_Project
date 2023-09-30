
package knusearch.clear.elasticsearch;

import knusearch.clear.elasticsearch.BasePostElasticsearchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticsearchService { //자바를 통해서 엘라스틱서치 조작(우분투 자체로는 불가능. es 조작 툴 필요. 종류는 다양하다)
    //우분투에서 es 켜놓고(start) http://localhost:9200/ 접속 시 es정보 뜸

        private final BasePostElasticsearchRepository elasticsearchRepository;

        @Transactional
        public List<BasePostElasticsearchEntity> searchPosts(String query) {
            return null;
        //return elasticsearchRepository.findByTitleOrText(query, query);
        }
}
