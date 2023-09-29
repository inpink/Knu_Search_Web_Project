package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.repository.post.PostIctRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostIctService {

    private final PostIctRepository postIctRepository;

    //여기도 추상화 필요
    public void savePostMain(BasePost postMain) {
        postIctRepository.save(postMain);
    }

    public String getBaseUrl() { return "https://sae.kangnam.ac.kr/menu/"; }

    public String[] getAllPostUrl(){
        return new String[] {"e408e5e7c9f27b8c0d5eeb9e68528b48.do",
                "e38fb5074d558dd5c570c62c9f36fdce.do"};
        //공지사항,  학부행사 등

    }
}
