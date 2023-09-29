package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.repository.post.PostMainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostMainService {

    private final PostMainRepository postMainRepository;

    public void savePostMain(BasePost postMain) {
        postMainRepository.save(postMain);
    }

    public String getBaseUrl() { return "https://web.kangnam.ac.kr/menu/"; }

    public String[] getAllPostUrl(){
        return new String[] {"f19069e6134f8f8aa7f689a4a675e66f.do",
                "e4058249224f49ab163131ce104214fb.do"};
        //공지사항,  행사/안내 등

    }
}
