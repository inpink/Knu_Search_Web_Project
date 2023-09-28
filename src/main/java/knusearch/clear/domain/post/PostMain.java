package knusearch.clear.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@DiscriminatorValue("0")
public class PostMain extends BasePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_main_id")
    private Long id;


    //==생성 메서드==//
    public static PostMain createPostMain(boolean scrtWrtiYn, String encMenuSeq, String encMenuBoardSeq,
                                             String title, String text, String image, LocalDate dateTime){
        PostMain postMain = new PostMain();

        postMain.setScrtWrtiYn(scrtWrtiYn);
        postMain.setEncMenuSeq(encMenuSeq);
        postMain.setEncMenuBoardSeq(encMenuBoardSeq);
        postMain.setTitle(title);
        postMain.setText(text);
        postMain.setImage(image);
        postMain.setDateTime(dateTime);

        return postMain;
    }
}
