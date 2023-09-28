package knusearch.clear.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) //상속관계 매핑 - 조인 전략
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public class Post extends BasePost { //크롤링한 데이터를 저장할  Post 객체
    //굳이 외래키 쓰는 것보다, 그때그때 join하고 / where로 검증하여 동기화 하는 것이 더 낫다.
    //괜히 외래키써서 오류날 수 있기 때문이라고 하심
    /*
    이 쿼리는 A와 B 테이블을 m_id 열을 기준으로 내부 조인하고, A 테이블에서 m_id 값이 5인 행만 선택
    SELECT A.*, B.*
    FROM A
    INNER JOIN B ON A.m_id = B.m_id
    WHERE A.m_id = 5;
     */

    /*
    B 테이블에서 A 테이블에 없는 m_id 값을 가진 행 삭제.
    이 작업을 주기적으로 실행하거나 데이터 변경 이벤트에 따라 트리거로 실행할 수 있음
    DELETE FROM B
WHERE m_id NOT IN (SELECT m_id FROM A);
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;


    //==생성 메서드==//
    //현재 사용되지 않고 있어서, basePost의 모든 필드를 담는 생성자가 아닐 수 있음
    public static Post createPost(String title, String text, String image, LocalDate dateTime){
        Post post = new Post();

        post.setTitle(title);
        post.setText(text);
        post.setImage(image);
        post.setDateTime(dateTime);

        return post;
    }

}

