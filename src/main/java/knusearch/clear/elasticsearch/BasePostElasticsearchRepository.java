package knusearch.clear.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

public interface BasePostElasticsearchRepository
        extends ElasticsearchRepository<BasePostElasticsearchEntity, String> {

    List<BasePostElasticsearchEntity> findByTitleOrText(String title, String text);

}
