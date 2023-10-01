package knusearch.clear.elasticsearch.repository;

import knusearch.clear.elasticsearch.domain.BasePostElasticsearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BasePostElasticsearchRepository
        extends ElasticsearchRepository<BasePostElasticsearchEntity, String> {

    List<BasePostElasticsearchEntity> findByTitleOrText(String title, String text);

}
