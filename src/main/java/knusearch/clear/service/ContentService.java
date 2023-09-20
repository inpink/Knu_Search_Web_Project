package knusearch.clear.service;


import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    public void crawlAndStoreData() {
        try {
            String url = "https://web.kangnam.ac.kr/menu/board/info/f19069e6134f8f8aa7f689a4a675e66f.do?encMenuSeq=ea2c9f2a785ae43e520c322896013dfe&encMenuBoardSeq=55d61695a5234f512d06484a5118bee4"; // 크롤링할 웹사이트 URL
            Document document = Jsoup.connect(url).get();

            // 데이터 추출
            // 원하는 div 요소 선택 (class가 "tbl_view"인 div를 선택)
            Element divElement = document.select(".tblw_subj").first();
            String data = divElement.text();  // div 내용 추출
            System.out.println("크롤링 제목:" + data);

            Element divElement2 = document.select(".tbl_view").first();
            String data2 = divElement2.text();  // div 내용 추출
            System.out.println("크롤링 본문:" + data2);

            // 이미지 태그 선택
            Elements imgElements = divElement2.select("img");

            // 이미지 소스 링크 추출
            for (Element imgElement : imgElements) {
                String src = imgElement.attr("src");
                System.out.println("크롤링 본문의 이미지 소스 링크:" + "https://web.kangnam.ac.kr"+src);
            }

            //Date 추출. span으로 묶여있어서 파싱으로 Date 형식만 가져옴
            Element divElement3 = document.select(".tblw_date").first();

            // 정규 표현식 패턴 설정
            Pattern pattern = Pattern.compile("등록날짜 (\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2})");
            Matcher matcher = pattern.matcher( divElement3.text());

            // 정규 표현식과 일치하는 부분 찾기
            if (matcher.find()) {
                // 그룹 1에서 일치하는 문자열 가져오기
                String data3 = matcher.group(1);
                System.out.println("크롤링 Date: " + data3);
            }


            // 추출한 데이터를 MySQL 데이터베이스에 저장하는 코드 추가


        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }

}
