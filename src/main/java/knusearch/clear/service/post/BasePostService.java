package knusearch.clear.service.post;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostIct;
import knusearch.clear.repository.post.BasePostRepository;
import knusearch.clear.service.CrawlService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public abstract class BasePostService<T extends BasePost> {

    //공통되는 부분
    protected final CrawlService crawlService;
    // postService들 -> cralwSerivce 접근OK,
    // 반대로  cralwSerivce -> postService는 절대 금지(순환참조). 순환참조는 하면 안 됨

    //Transactional을 먹여줘서, CrawlController의 하나의 method에서 요구한 모든 작업이 끝난 뒤에 DB에 Commit된다!
    //CrawlController의 하나의 메소드에서는 postIctService.crawlUpdate(); 이런식으로 호출되었다.
    //이 메소드가 완전히 끝나야만 DB에 Commit된다. 그 전에 작업 중에는 repo.save가 실행돼도 실제로 DB에 반영되지 않는다는 것이다!


    @Transactional
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

    @Transactional
    public void checkAndSave(BasePost basePost) {

        String encMenuSeq=basePost.getEncMenuSeq();
        String encMenuBoardSeq=basePost.getEncMenuBoardSeq();

        //DB에 없는 것만 추가!!!
        if (getRepository().findAllByEnc(encMenuSeq,encMenuBoardSeq).size()==0){
            crawlService.setPostValues(basePost);

            // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가
            getRepository().save(basePost); //★
        }
    }

    @Transactional
    public void savePost(BasePost basePost) {
        getRepository().save(basePost);
    }

    @Transactional
    public int findPostTextLen(long id){
        int len=0;

        //JPA의 em.find 메서드를 사용하여 엔티티를 검색할 때, 해당 ID에 해당하는 엔티티가 데이터베이스에 없는 경우 null을 반환
        BasePost basePost = getRepository().findOne(id);
        if (basePost !=null) { //따라서 null 여부를 확인하여 NullPointerException을 방지할 수 있다
            String text= basePost.getText();
            len=text.length();
        }

        return len;
    }


    //각 PostService에서 구현해줘야 할 것들
    abstract public T getNewPostInstance();

    abstract public BasePostRepository getRepository();


    abstract public String getBaseUrl();

    abstract public String[] getAllPostUrl();

}
