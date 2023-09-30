package knusearch.clear.jpa.service;


import knusearch.clear.jpa.domain.post.BasePost;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CrawlService {

    @Transactional
    public String makeFinalPostListUrl(String baseUrl, String postUrl, int pageIdx){
        return baseUrl + postUrl + "?paginationInfo.currentPageNo=" + pageIdx;
    }

    @Transactional
    public String makeFinalPostUrl(String baseUrl, String postUrl,
                                   boolean scrtWrtiYn, String encMenuSeq, String encMenuBoardSeq ){
        return baseUrl + "board/info/" + postUrl
                + "?scrtWrtiYn="+scrtWrtiYn+"&encMenuSeq="
                + encMenuSeq + "&encMenuBoardSeq=" + encMenuBoardSeq;
    }

    //@Transactional : 트랜잭션 생성.
    //@Transactional 어노테이션이 붙은 메서드나 클래스 내부에서 실행되는 모든 데이터베이스 연산은 하나의 트랜잭션 내에서 실행됨
    //즉, 하나의 트랜잭션에서 모든 연산이 성공적으로 완료되거나 오류가 발생할 때 전체를 롤백할 수 있음.
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
    public Elements GetAllLinksFromOnePage(String baseUrl, String postUrl, int pageIdx) {  //하나의 페이지에서 모든 게시물들 링크뽑아냄

        //전체를 담을 List (현재 사용 X)
        //List<BasePost> postList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(
                    makeFinalPostListUrl(baseUrl,postUrl,pageIdx)).get();

            // 게시물 목록에서 각 게시물의 URL을 추출
            Element div1 = document.select(".sec_inner").first();
            Elements detailLink = div1.select(".detailLink");
            // "data-params" 속성 값을 가져오기

            return detailLink;

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public void setURLValues(BasePost basePost, Element linkElement, String baseUrl, String postUrl){
        String dataParams = linkElement.attr("data-params");
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

    }

    @Transactional
    public void setPostValues(BasePost basePost) { //하나의 게시물에서 제목, 본문, 링크, 날짜 가져오기
        try {
            String finalURL = basePost.getUrl();
            Document document = Jsoup.connect(finalURL).get();

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
            log.info("글자 길이가"+koreanLen+"를 초과하는 경우 cut되었습니다"+text.length());
            return text.substring(0,koreanLen);
        }

        return text;
    }

}
