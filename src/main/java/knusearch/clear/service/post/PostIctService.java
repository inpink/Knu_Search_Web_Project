package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostIct;
import knusearch.clear.repository.post.PostIctRepository;
import knusearch.clear.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostIctService implements BasePostService<PostIct> {

    private final PostIctRepository postIctRepository;
    private final CrawlService crawlService; // postService들 -> cralwSerivce 접근OK,
    // 반대로  cralwSerivce -> postService는 절대 금지(순환참조). 순환참조는 하면 안 됨

    public void crawlUpdate(){
        String baseUrl = getBaseUrl();
        String[] allPostUrl = getAllPostUrl();

        for (String postUrl : allPostUrl)
        {
            String firsNoticetUrl = crawlService.makeFinalPostListUrl(baseUrl,postUrl,1);
            int totalPageIdx = crawlService.totalPageIdx(firsNoticetUrl); //총 페이지수 구해옴

            //for (int i = 1; i <= totalPageIdx; i++) { //너무 많으니까 일단 10개정도로 테스트
            for (int i = 1; i <= 10; i++) {

                //굳이 안받아와도 되긴할듯 필요하면 받아오고 //상속관계를 이용하여 BaseContent로 통일!
                //추상화를 통해 DIP(의존관계역전) 적용된 케이스임
                //List<BasePost> contentList = scrapeWebPage(baseUrl, postUrl ,i); //10페이지에 있는 것 contentMain에 저장시킴?
                Elements links= crawlService.GetAllLinksFromOnePage(baseUrl, postUrl ,i);

                for (Element linkElement : links) {
                    BasePost basePost= getNewPostInstance();
                    crawlService.setURLValues(basePost, linkElement, baseUrl, postUrl);

                    checkAndSave(basePost);
                }

                System.out.println(i + "번째 페이지에 있는 모든 게시글 크롤링");
            }
        }

    }

    //현재는 이미 크롤링한 글은 크롤링하지 않도록 했음. 만약 크롤링한 글인데 그 글이 업데이트 된다면? 이를 보완하기 위해
    //한번 싹 삭제하고 다시 크롤링하거나, 달라진 내용 있는 지 검사해서 달라진 것만 업데이트 하거나. DB 비용 고려해서 추후 시간남으면 구현할 것. 지금은 일단 그대로 감
    @Override
    public void checkAndSave(BasePost basePost) {

        String finalURL = basePost.getUrl();
        String encMenuSeq=basePost.getEncMenuSeq();
        String encMenuBoardSeq=basePost.getEncMenuBoardSeq();

        //DB에 없는 것만 추가!!!
        if (postIctRepository.findAllByEnc(encMenuSeq,encMenuBoardSeq).size()==0){
            crawlService.setPostValues(basePost);

            // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가
            postIctRepository.save(basePost); //★
        }
    }

    @Override
    public PostIct getNewPostInstance() {
        return new PostIct();
    }

    @Override
    public void savePost(BasePost basePost) {
        postIctRepository.save(basePost);
    }

    @Override
    public String getBaseUrl() { return "https://sae.kangnam.ac.kr/menu/"; }

    @Override
    public String[] getAllPostUrl(){
        return new String[] {"e408e5e7c9f27b8c0d5eeb9e68528b48.do",
                "e38fb5074d558dd5c570c62c9f36fdce.do"};
        //공지사항,  학부행사 등

    }

    @Override
    public int findTextLen(long id){
        int len=0;

        //JPA의 em.find 메서드를 사용하여 엔티티를 검색할 때, 해당 ID에 해당하는 엔티티가 데이터베이스에 없는 경우 null을 반환
        BasePost basePost = postIctRepository.findOne(id);
        if (basePost !=null) { //따라서 null 여부를 확인하여 NullPointerException을 방지할 수 있다
            String text= basePost.getText();
            len=text.length();
        }

        return len;
    }
}
