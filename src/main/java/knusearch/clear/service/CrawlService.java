package knusearch.clear.service;


import knusearch.clear.domain.content.BaseContent;
import knusearch.clear.domain.content.Content;
import knusearch.clear.domain.content.ContentMain;
import knusearch.clear.repository.content.ContentMainRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrawlService { //이부분은 사용자가 MVC로 접근하는 것이 아닌데 내부 내용들을 어디에 둬야할지 고민이네

    //public void updateCrawlData(){
//}
    private final ContentMainRepository contentMainRepository; //★우선 ContentMain 기준으로 함.
    // 다른 사이트들 어떻게 다룰 것인지 고민필요. 아마 사이트별로 각각 DB를 둘거면 Repo, Serivce도 각각 필요할듯.
    //Crawling 부분은 다 여기 두고.

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
    public List<BaseContent> scrapeWebPage(String url) {  //하나의 페이지에서 모든 게시물들 링크뽑아냄

        //전체를 담을 List
        List<BaseContent> contentList = new ArrayList<>();

        try {

            Document document = Jsoup.connect(url).get();

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
                BaseContent content= new ContentMain(); //★하나의 행 생성

                String dataParams = link.attr("data-params");
                /*System.out.println("dataParams"+dataParams);*/

                // JSON 파싱
                JSONObject jsonObject = new JSONObject(dataParams);

                // "scrtWrtiYn"와 "encMenuSeq"와 "encMenuBoardSeq" 값을 가져오기
                Boolean scrtWrtiYn = jsonObject.getBoolean("scrtWrtiYn"); //얘는 boolean
                String encMenuSeq = jsonObject.getString("encMenuSeq");
                String encMenuBoardSeq = jsonObject.getString("encMenuBoardSeq");

                // 최종 URL을 생성
                String baseURL = "https://web.kangnam.ac.kr/menu/board/info/f19069e6134f8f8aa7f689a4a675e66f.do";
                String finalURL = baseURL + "?scrtWrtiYn"+scrtWrtiYn+"&encMenuSeq=" + encMenuSeq + "&encMenuBoardSeq=" + encMenuBoardSeq;

                /*// 결과 출력
                System.out.println("encMenuSeq: " + encMenuSeq);
                System.out.println("encMenuBoardSeq: " + encMenuBoardSeq);
                System.out.println("Final URL: " + finalURL); //하나의 페이지에서 모든 게시물들 링크*/

                content.setScrtWrtiYn(scrtWrtiYn);
                content.setEncMenuSeq(encMenuSeq);
                content.setEncMenuBoardSeq(encMenuBoardSeq);

                crawlAndStoreData(content,finalURL);

                contentMainRepository.save(content); //★

            }

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return contentList;
    }


    public void crawlAndStoreData(BaseContent content, String url) { //하나의 게시물에서 제목, 본문, 링크, 날짜 가져오기
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
            for (Element imgElement : imgElements) {
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

            // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가
            content.setTitle(title);
            content.setText(cutText(text));
            content.setImage(imageSrc);
            content.setDateTime(dateTime);

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }

    //글자수가 1000Byte를 초과하는 경우 cut하기. 이런 의미에서도 검색을 위해 본문은 그냥 NoSQL를 쓰는 게 좋을 듯..
    public String cutText(String text){
        if (text.length()>250) return text.substring(0,250);

        return text;
    }

    @Transactional
    public int findTextLen(long id){
        BaseContent baseContent = contentMainRepository.findOne(id);
        String text=baseContent.getText();

        return text.length();
    }

}
