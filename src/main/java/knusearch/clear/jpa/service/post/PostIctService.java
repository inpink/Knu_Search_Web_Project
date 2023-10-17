package knusearch.clear.jpa.service.post;

import knusearch.clear.jpa.domain.post.PostIct;
import knusearch.clear.jpa.repository.post.BasePostRepository;
import knusearch.clear.jpa.repository.post.PostIctRepository;
import knusearch.clear.jpa.service.CrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PostIctService extends BasePostService<PostIct> {

    private final PostIctRepository postIctRepository;

    public PostIctService(CrawlService crawlService, PostIctRepository postIctRepository) {
        super(crawlService);
        this.postIctRepository = postIctRepository;
    }

    @Override
    public PostIct getNewPostInstance() {
        return new PostIct();
    }

    @Override
    public BasePostRepository getRepository() {
        return postIctRepository;
    }

    @Override
    public String getBaseUrl() { return "https://sae.kangnam.ac.kr/menu/"; }

    @Override
    public String[] getAllPostUrl(){
        return new String[] {"e408e5e7c9f27b8c0d5eeb9e68528b48.do",
                "e38fb5074d558dd5c570c62c9f36fdce.do",
                "4560109aa534f393e3f41711841b2e6b.do"
        };


        //공지사항,  학부행사, 졸업작품 등

    }

}
