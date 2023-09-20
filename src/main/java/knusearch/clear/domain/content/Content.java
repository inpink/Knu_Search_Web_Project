package knusearch.clear.domain.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) //상속관계 매핑 - 조인 전략
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public class Content extends BaseContent { //크롤링한 데이터를 저장할  Content 객체

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;


    //==생성 메서드==//
    public static Content createContent(String title, String text, String image, Date dateTime){
        Content content = new Content();

        content.setTitle(title);
        content.setText(text);
        content.setImage(image);
        content.setDateTime(dateTime);

        return content;
    }

}

