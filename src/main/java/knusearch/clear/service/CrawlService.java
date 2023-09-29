package knusearch.clear.service;


import knusearch.clear.domain.post.BasePost;
import knusearch.clear.domain.post.PostIct;
import knusearch.clear.domain.post.PostMain;
import knusearch.clear.repository.post.PostIctRepository;
import knusearch.clear.repository.post.PostMainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CrawlService { //이부분은 사용자가 MVC로 접근하는 것이 아닌데 내부 내용들을 어디에 둬야할지 고민이네

    //public void updateCrawlData(){
//}
    private final PostMainRepository postMainRepository; //★우선 PostMain 기준으로 함.
    private final PostIctRepository postIctRepository;
    // 다른 사이트들 어떻게 다룰 것인지 고민필요. 아마 사이트별로 각각 DB를 둘거면 Repo, Serivce도 각각 필요할듯.
    //Crawling 부분은 다 여기 두고.

    @Transactional
    public void update(String baseUrl, String[] allPostUrl){
        for (String postUrl : allPostUrl )
        {
            // 웹 페이지의 URL (아래는 공지사항 첫페이지. currentPageNo만 바뀌면 됨)
            //TODO: CrawlService가 담당하는 부분과, 중복&반복되는 부분을 각 MainPostService, POstICTService로 분리할 때, 변수에 main이라는 이름 있으면 빼버리기
            String firsNoticetUrl = makeFinalPostListUrl(baseUrl,postUrl,1);
            int totalPageIdx = totalPageIdx(firsNoticetUrl);

            //for (int i = 1; i <= totalPageIdx; i++) { //너무 많으니까 일단 10개정도로 테스트
            for (int i = 1; i <= 10; i++) {
                //굳이 안받아와도 되긴할듯 필요하면 받아오고 //상속관계를 이용하여 BaseContent로 통일!
                //추상화를 통해 DIP(의존관계역전) 적용된 케이스임
                List<BasePost> contentList = scrapeWebPage(baseUrl, postUrl ,i); //10페이지에 있는 것 contentMain에 저장시킴?

                //★모든 것이 의존관계역전(DIP)이 적용될 수 없고, 이유에 따라 tradeoff가 필요 (책 객오사)
                for (BasePost content : contentList) {
                    System.out.println("크롤링 확인:" + content.getTitle()); //잘되고
                    // 아직 일부(제목,본문)만 저장해서 다 저장할 수 있게 추가해야 하고,

                    //contentMainService.saveContentMain(content); //저장 잘 됨. 근데 저장은 service에서 해주자
                }

                System.out.println(i + "번째 페이지에 있는 모든 게시글 크롤링");

            }
        }
    }

    public String makeFinalPostListUrl(String baseUrl, String postUrl, int pageIdx){
        return baseUrl + postUrl + "?paginationInfo.currentPageNo=" + pageIdx;
    }

    public String makeFinalPostUrl(String baseUrl, String postUrl,
                                   boolean scrtWrtiYn, String encMenuSeq, String encMenuBoardSeq ){
        return baseUrl + "board/info/" + postUrl
                + "?scrtWrtiYn="+scrtWrtiYn+"&encMenuSeq="
                + encMenuSeq + "&encMenuBoardSeq=" + encMenuBoardSeq;
    }

    @Transactional
    public int totalPageIdx(String url){ //하나의 게시판에서 모든 페이지 수 구함
        try {
            Document document = Jsoup.connect(url).get();
            Element div1 = document.select("div.total_idx").first();
            Element spanElement = div1.select("span").first();

            String spanText = spanElement.text();
            System.out.println("spanText = " + spanText);

            // "/"를 기준으로 문자열을 분할
            String[] parts = spanText.split("/");

            // 분할된 문자열 중 두 번째 부분을 추출하고 공백 제거
            String numberPart = parts[1].trim();
            int totalPageIdx = Integer.parseInt(numberPart);

            // 결과 출력
            System.out.println("Extracted Number: " + totalPageIdx);
            return totalPageIdx;
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return 0;
    }

    @Transactional
    public List<BasePost> scrapeWebPage(String baseUrl, String postUrl, int pageIdx) {  //하나의 페이지에서 모든 게시물들 링크뽑아냄

        //전체를 담을 List
        List<BasePost> postList = new ArrayList<>();

        try {

            Document document = Jsoup.connect(
                    makeFinalPostListUrl(baseUrl,postUrl,pageIdx)).get();

            // 게시물 목록에서 각 게시물의 URL을 추출
            Element div1 = document.select(".sec_inner").first();
            Elements detailLink = div1.select(".detailLink");
            // "data-params" 속성 값을 가져오기

            /*System.out.println("div1:" + div1);
            System.out.println("detailLink:" + detailLink);*/

            // 정규표현식을 사용하여 encMenuSeq와 encMenuBoardSeq 값을 추출
            String encMenuSeqPattern = "encMenuSeq\":\"(.*?)\"";
            String encMenuBoardSeqPattern = "encMenuBoardSeq\":\"(.*?)\"";


            for (Element link : detailLink) {
                BasePost basePost= new PostIct(); //★하나의 행 생성

                String dataParams = link.attr("data-params");
                /*System.out.println("dataParams"+dataParams);*/

                // JSON 파싱
                JSONObject jsonObject = new JSONObject(dataParams);

                // "scrtWrtiYn"와 "encMenuSeq"와 "encMenuBoardSeq" 값을 가져오기
                // ↓기본값으로 false를 설정. 어떤 사이트에는 scrtWrtiYn값이 없다. scrtWrtiYn는 상위 노출되는 공지유무를 뜻함
                Boolean scrtWrtiYn = jsonObject.optBoolean("scrtWrtiYn",false);  //얘는 boolean
                String encMenuSeq = jsonObject.getString("encMenuSeq");
                String encMenuBoardSeq = jsonObject.getString("encMenuBoardSeq");

                // 최종 URL을 생성
                String finalURL = makeFinalPostUrl(baseUrl,postUrl,scrtWrtiYn,encMenuSeq,encMenuBoardSeq);

                basePost.setUrl(finalURL);
                basePost.setScrtWrtiYn(scrtWrtiYn);
                basePost.setEncMenuSeq(encMenuSeq);
                basePost.setEncMenuBoardSeq(encMenuBoardSeq);
                //System.out.println("finalURL = " + finalURL);

                //DB에 없는 것만 추가!!!
                if (findAllByEnc(encMenuSeq,encMenuBoardSeq).size()==0){
                    crawlAndStoreData(basePost,finalURL);

                    // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가
                    postIctRepository.save(basePost); //★
                }

            }

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return postList;
    }


    public void crawlAndStoreData(BasePost basePost, String url) { //하나의 게시물에서 제목, 본문, 링크, 날짜 가져오기
        try {
            Document document = Jsoup.connect(url).get();

            // 데이터 추출
            // 원하는 div 요소 선택 (class가 "tbl_view"인 div를 선택)
            Element divElement = document.select(".tblw_subj").first();
            String title = divElement.text();  // div 내용 추출
            /*System.out.println("크롤링 제목:" + title);*/

            Element divElement2 = document.select(".tbl_view").first();
            String text = divElement2.text();  // div 내용 추출
            /*System.out.println("크롤링 본문:" + text);*/

            // 이미지 태그 선택
            Elements imgElements = divElement2.select("img");

            // 이미지 소스 링크 추출
            String imageSrc = null;
            for (Element imgElement : imgElements) { //여러개면 여러개 다 뽑아냄. 일단 지금은 db에 마지막 1개만 담고있음
                imageSrc = imgElement.attr("src");
                /*System.out.println("크롤링 본문의 이미지 소스 링크:" + "https://web.kangnam.ac.kr" + imageSrc);*/
            }

            //Date 추출. span으로 묶여있어서 파싱으로 Date 형식만 가져옴
            Element divElement3 = document.select(".tblw_date").first();

            // 정규 표현식 패턴 설정
            Pattern pattern = Pattern.compile("등록날짜 (\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2})");
            Matcher matcher = pattern.matcher(divElement3.text());

            // 정규 표현식과 일치하는 부분 찾기
            String dateString = null;
            if (matcher.find()) {
                // 그룹 1에서 일치하는 문자열 가져오기
                dateString = matcher.group(1);
                /*System.out.println("크롤링 Date: " + dateTime);*/
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            LocalDate dateTime = LocalDate.parse(dateString, formatter);

            basePost.setTitle(title);
            basePost.setText(cutString(text,BasePost.TEXT_COLUMN_LENGTH));
            basePost.setImage(cutString(imageSrc,BasePost.IMAGE_COLUMN_LENGTH));
            basePost.setDateTime(dateTime);

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }

    //글자수가 len*4 Byte를 초과하는 경우 cut하기.
    public String cutString(String text, int byteSize){
        int koreanLen=byteSize/4;
        if (text!=null && text.length()>koreanLen) {
            log.info("글자 길이가"+koreanLen+"를 초과하는 경우 cut되었습니다"+text.length()+text);
            return text.substring(0,koreanLen);
        }

        return text;
    }

    @Transactional
    public int findTextLen(long id){
        int len=0;

        //JPA의 em.find 메서드를 사용하여 엔티티를 검색할 때, 해당 ID에 해당하는 엔티티가 데이터베이스에 없는 경우 null을 반환
        BasePost basePost = postMainRepository.findOne(id);
        if (basePost !=null) { //따라서 null 여부를 확인하여 NullPointerException을 방지할 수 있다
            String text= basePost.getText();
            len=text.length();
        }

        return len;
    }

    //이 내용은 추후 postMainService에 둬야할듯. 각 사이트 service마다 각각 두기
    public List<PostIct> findAllByEnc(String encMenuSeq, String encMenuBoardSeq){
        System.out.println(encMenuSeq+" "+encMenuBoardSeq);
        System.out.println("찾은 리스트"+ postIctRepository.findAllByEnc(encMenuSeq,encMenuBoardSeq).size());
        //return postMainRepository.findAllByEnc(encMenuSeq,encMenuBoardSeq);
        return postIctRepository.findAllByEnc(encMenuSeq,encMenuBoardSeq);
        //TODO: 이 부분, List 반환 형식때문에 제네릭써서 BasePostService만들고 각각 둬야함! 일단 주석처리해서 테스트만 했음
    }

}
