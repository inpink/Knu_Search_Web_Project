package knusearch.clear.jpa.domain.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Document(indexName = "member")
@Mapping(mappingPath = "elastic/member-mapping.json")
@Setting(settingPath = "elastic/member-setting.json")
public class MemberDocument { //RDB JPA를 위한 Member와, NoSQL 엘라스틱서치를 위한 MemberDocument 분리.
    //엘라스틱서치에는 @Entity @Column(name = "member_id")같은거 못써줌

}
