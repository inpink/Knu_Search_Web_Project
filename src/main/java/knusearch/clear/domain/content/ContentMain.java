package knusearch.clear.domain.content;

import jakarta.persistence.*;
import knusearch.clear.domain.Search;
import knusearch.clear.domain.SearchSite;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@DiscriminatorValue("0")
public class ContentMain extends BaseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_main_id")
    private Long id;


    //==생성 메서드==//
    public static ContentMain createContentMain(boolean scrtWrtiYn, String encMenuSeq, String encMenuBoardSeq,
                                                String title, String text, String image, Date dateTime){
        ContentMain contentMain = new ContentMain();

        contentMain.setScrtWrtiYn(scrtWrtiYn);
        contentMain.setEncMenuSeq(encMenuSeq);
        contentMain.setEncMenuBoardSeq(encMenuBoardSeq);
        contentMain.setTitle(title);
        contentMain.setText(text);
        contentMain.setImage(image);
        contentMain.setDateTime(dateTime);

        return contentMain;
    }
}
