package knusearch.clear.elasticsearch.domain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setting(settingPath = "/static/elastic-settings.json")
@Document(indexName = "basepost-index") // Elasticsearch 인덱스 이름 설정
public class BasePostElasticsearchEntity {

    @Id // Elasticsearch 문서 ID
    private String id;

    @Field(type = FieldType.Text, name = "title") // Elasticsearch 필드 설정
    private String title;

    @Field(type = FieldType.Text, name = "text")
    private String text;

    @Field(type = FieldType.Text, name = "image")
    private String image;

    @Field(type = FieldType.Boolean, name = "scrt_wrti_yn")
    private boolean scrtWrtiYn;

    @Field(type = FieldType.Text, name = "enc_menu_seq")
    private String encMenuSeq;

    @Field(type = FieldType.Text, name = "enc_menu_board_seq")
    private String encMenuBoardSeq;

    @Field(type = FieldType.Text, name = "url")
    private String url;

    @Field(type = FieldType.Date, name = "date_time")
    private LocalDate dateTime;


}