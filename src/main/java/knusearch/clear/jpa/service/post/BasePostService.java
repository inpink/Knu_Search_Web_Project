package knusearch.clear.jpa.service.post;

import java.util.List;
import java.util.Optional;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.repository.post.BasePostRepository;
import knusearch.clear.jpa.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasePostService {

    //공통되는 부분
    private final CrawlService crawlService;
    // postService들 -> cralwSerivce 접근OK,
    // 반대로  cralwSerivce -> postService는 절대 금지(순환참조). 순환참조는 하면 안 됨
    private final BasePostRepository basePostRepository;

    //Transactional을 먹여줘서, CrawlController의 하나의 method에서 요구한 모든 작업이 끝난 뒤에 DB에 Commit된다!
    //CrawlController의 하나의 메소드에서는 postIctService.crawlUpdate(); 이런식으로 호출되었다.
    //이 메소드가 완전히 끝나야만 DB에 Commit된다. 그 전에 작업 중에는 repo.save가 실행돼도 실제로 DB에 반영되지 않는다는 것이다!

    @Transactional
    public void crawlUpdate() { // crawl and make baseposts
        String baseUrl = getBaseUrl();
        String[] allPostUrl = getAllPostUrl();

        for (String postUrl : allPostUrl) {
            System.out.println("postUrl = " + postUrl);
            String firsNoticetUrl = crawlService.makeFinalPostListUrl(baseUrl, postUrl, 1);
            int totalPageIdx = crawlService.totalPageIdx(firsNoticetUrl); //총 페이지수 구해옴

            //for (int i = 1; i <= totalPageIdx; i++) {
            for (int i = 1; i <= 2; i++) { //너무 많으니까 일단 10개정도로 테스트

                //굳이 안받아와도 되긴할듯 필요하면 받아오고 //상속관계를 이용하여 BaseContent로 통일!
                //추상화를 통해 DIP(의존관계역전) 적용된 케이스임
                //List<BasePost> contentList = scrapeWebPage(baseUrl, postUrl ,i); //10페이지에 있는 것 contentMain에 저장시킴?
                Elements links = crawlService.GetAllLinksFromOnePage(baseUrl, postUrl, i);

                for (Element linkElement : links) {
                    BasePost basePost = new BasePost();
                    crawlService.setURLValues(basePost, linkElement, baseUrl, postUrl);

                    //TODO: Transcational을 없애고, 아래 하나 완료될 때마다 바로 저장되도록
                    checkAndSave(basePost);
                }

                System.out.println(i + "번째 페이지에 있는 모든 게시글 크롤링");
            }
        }
    }

    //TODO: 현재는 이미 추가된 게시글이면 update 안함. 추후, 본문이 수정된 경우나 글이 삭제된 경우도 커버할 수 있게끔 구현할 수 있음.
    // : 전체 삭제하고 다시 업로드(자원이 많이 들긴 하겠다만). 트랜잭션에 의해 재업하는 동안에도 문제없음. "테이블 행 싹 비우는 코드 java에서 하고 -> 모두 다시 업로드" 하는 방식 이용
    @Transactional
    public void checkAndSave(BasePost basePost) {

        String encMenuSeq = basePost.getEncryptedMenuSequence();
        String encMenuBoardSeq = basePost.getEncryptedMenuBoardSequence();

        //DB에 없는 것만 추가!!!
        if (basePostRepository.findAllByEncryptedMenuSequenceAndEncryptedMenuBoardSequence(encMenuSeq, encMenuBoardSeq).size() == 0) {
            crawlService.setPostValues(basePost);

            // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가
            basePostRepository.save(basePost); //★
        }
    }

    @Transactional
    public void savePost(BasePost basePost) {
        basePostRepository.save(basePost);
    }

    @Transactional
    public int findPostTextLen(long id) {
        int len = 0;

        //JPA의 em.find 메서드를 사용하여 엔티티를 검색할 때, 해당 ID에 해당하는 엔티티가 데이터베이스에 없는 경우 null을 반환
        Optional<BasePost> basePost = basePostRepository.findById(id);
        if (basePost != null) { //따라서 null 여부를 확인하여 NullPointerException을 방지할 수 있다
            String text = basePost.get().getText();
            len = text.length();
        }

        return len;
    }

    @Transactional
    public List<BasePost> findAll() {
        return basePostRepository.findAll();
    }

    @Transactional
    public void updateClassification(BasePost basePost, String classification) {
        basePost.setClassification(classification);
    }

    public String getBaseUrl() {
        return "https://web.kangnam.ac.kr/";
        //return Site.findBaseUrl(basePost.get);
    } // TODO:

    public String[] getAllPostUrl() {
        return new String[]{"f19069e6134f8f8aa7f689a4a675e66f.do",
                "e4058249224f49ab163131ce104214fb.do"};
        //공지사항,  행사/안내 등
    }
}
