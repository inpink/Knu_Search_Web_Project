package knusearch.clear.jpa.repository.post;

import java.util.List;
import knusearch.clear.jpa.domain.post.BasePost;

public interface BasePostRepository<T extends BasePost> {
    //새로운 사이트를 추가할 때 꼭 추가해야할 공통된 기능들을 알려주는 지표가 됨
    //EntityManager까지 여기서 적어주면 private를 써줘야 하고, abstract class를 써야 하는데
    //em까지 상속된 클래스에 안적혀 있고, super(em)이런식으로 생성자 코드가 적혀있는 게
    //오히려 가독성이 안좋을 것이라 생각되어서 em은 구현클래스에서 각각 쓰는걸로 함
    //추후 다중 상속과 API 디자인을 고려하여 abstract class 대신 inteface로 결정

    //각 상속받는 곳에서 쓰는 타입이 다르기 때문에 제네릭T 사용함! BasePost의 자식만 T가 될 수 있음!

    void save(BasePost basePost);

    T findOne(Long id);

    List<T> findAllByEnc(String encMenuSeq, String encMenuBoardSeq);

    List<T> findAll();
}
