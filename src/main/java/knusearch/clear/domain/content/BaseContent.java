package knusearch.clear.domain.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter @Setter
public abstract class BaseContent { //게시글 테이블에 공통적으로 쓰이는 필드를 담음
    //URL3종(scrtWrtiYn, encMenuSeq,encMenuBoardSeq) 제목, 본문, 이미지 링크(여러개 할거면 또 테이블 필요함), 날짜, 사이트 번호
    //추상클래스!

    private boolean scrtWrtiYn;

    private String encMenuSeq;

    private String encMenuBoardSeq;

    private String title;

    private String text;

    private String image;  //일단 1개만 담게. 여러개 할거면 또 image 테이블 필요함. 1대 다 구조

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;


}
