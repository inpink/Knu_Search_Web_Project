package knusearch.clear.service;

/*
readOnly 속성은 해당 메서드에서 데이터베이스의 읽기 작업만 수행하고, 쓰기 작업은 하지 않음을 나타냅니다.
이렇게 설정된 메서드는 트랜잭션 커밋 시에 롤백되는 것을 방지하고, 데이터베이스에 대한 읽기 작업을 최적화할 수 있습니다.
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {


}
