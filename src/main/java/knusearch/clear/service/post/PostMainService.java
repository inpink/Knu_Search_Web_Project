package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostMain;
import knusearch.clear.repository.post.BasePostRepository;
import knusearch.clear.repository.post.PostMainRepository;
import knusearch.clear.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PostMainService extends BasePostService<PostMain> {

    private final PostMainRepository postMainRepository;

    public PostMainService(CrawlService crawlService, PostMainRepository postMainRepository) {
        super(crawlService);
        this.postMainRepository = postMainRepository;
    }

    public void savePostMain(BasePost postMain) {
        postMainRepository.save(postMain);
    }


    @Override
    public PostMain getNewPostInstance() {
        return new PostMain();
    }

    @Override
    public BasePostRepository getRepository() {
        return postMainRepository;
    }

    @Override
    public String getBaseUrl() { return "https://web.kangnam.ac.kr/menu/"; }

    @Override
    public String[] getAllPostUrl(){
        return new String[] {"f19069e6134f8f8aa7f689a4a675e66f.do",
                "e4058249224f49ab163131ce104214fb.do"};
        //공지사항,  행사/안내 등

    }
}
