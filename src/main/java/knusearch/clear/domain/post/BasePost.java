package knusearch.clear.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter @Setter
public abstract class BasePost { //게시글 테이블에 공통적으로 쓰이는 필드를 담음(추상화)  //많은 경우 추상화를 통해 의존관계 역전을 달성하는 것이 일반적
    //URL3종(scrtWrtiYn, encMenuSeq,encMenuBoardSeq) 제목, 본문, 이미지 링크(여러개 할거면 또 테이블 필요함), 날짜, 사이트 번호
    //추상클래스!

    //자바에서 abstract class와 default interface 둘 다를 쓸 수 있으면 후자를 선택하는 이유
    // => 대표적으로 다중 상속 가능, API 디자인 가능
    // => 생성자가 꼭 필요한 경우에는 abstract class 사용할 것
    // => 하지만 Effective Java에서 생성자 조차 "정적 팩터리 메서드"로 대체하는 것을 추천한다고 했음
    // => 하지만 여기서는 private field가 필요하므로 abstract class를 썼다
    //결론 : 상황에 맞게 선택할 것

    private String url;

    private boolean scrtWrtiYn;

    private String encMenuSeq;

    private String encMenuBoardSeq;

    private String title;

    //JPA에서 String을 주면 기본적으로 255Byte varchar이 된다.
    // UTF-8 인코딩을 기준으로 255바이트로는 약 63~85글자의 한글을 담을 수 있음. 한글자에3~4byte이기 때문
    //열의 크기를 지나치게 크게 설정하면 데이터베이스 디스크 공간을 낭비하게 된다.
    // 모든 데이터를 담으려면 분할하거나, 저장공간 많이 쓰거나 고민이 필요
    @Column(length=2000)
    private String text;

    @Column(length=2000)
    private String image;  //일단 1개만 담게. 여러개 할거면 또 image 테이블 필요함. 1대 다 구조

    //만약 시간 정보를 함께 저장하려면 LocalDate말고 java.time.LocalDateTime을 사용하고 TemporalType.TIMESTAMP로 매핑
    //유형 일치 중요
    @Temporal(TemporalType.DATE)
    private LocalDate dateTime;


}
