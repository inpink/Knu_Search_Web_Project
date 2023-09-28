package knusearch.clear.controller;

import knusearch.clear.domain.content.BaseContent;
import knusearch.clear.domain.content.Content;
import knusearch.clear.domain.content.ContentMain;
import knusearch.clear.service.CrawlService;
import knusearch.clear.service.content.ContentMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlController {

    private final CrawlService crawlService;
    private final ContentMainService contentMainService;

    //@GetMapping("/")

    @GetMapping("/mainCrawlUpdate")
    public String mainCrawlUpdate() {

        String[] allMainBaseUrl = contentMainService.getAllBaseUrl();

        for (String mainBaseUrl : allMainBaseUrl )
        {
            // 웹 페이지의 URL (아래는 공지사항 첫페이지. currentPageNo만 바뀌면 됨)
            String firsNoticetUrl = mainBaseUrl + 1;
            int totalPageIdx = crawlService.totalPageIdx(firsNoticetUrl);

            //for (int i = 1; i <= totalPageIdx; i++) { //너무 많으니까 일단 10개정도로 테스트
            for (int i = 1; i <= 10; i++) {
                //굳이 안받아와도 되긴할듯 필요하면 받아오고 //상속관계를 이용하여 BaseContent로 통일!
                //추상화를 통해 DIP(의존관계역전) 적용된 케이스임
                List<BaseContent> contentList = crawlService.scrapeWebPage(mainBaseUrl + i); //10페이지에 있는 것 contentMain에 저장시킴?

                //★모든 것이 의존관계역전(DIP)이 적용될 수 없고, 이유에 따라 tradeoff가 필요 (책 객오사)
                for (BaseContent content : contentList) {
                    System.out.println("크롤링 확인:" + content.getTitle()); //잘되고
                    // 아직 일부(제목,본문)만 저장해서 다 저장할 수 있게 추가해야 하고,

                    //contentMainService.saveContentMain(content); //저장 잘 됨. 근데 저장은 service에서 해주자
                }

                System.out.println(i + "번째 페이지에 있는 모든 게시글 크롤링");

            }
        }


        /*
        120게시물 담았을때 SHOW TABLE STATUS like 'content_main';로 확인해 봤을때
        Data_length가 98304=98,304 바이트=약 96 KB
         */

        System.out.println("공지사항 게시판 - 크롤링 업데이트 완료!"); //2023-09-25기준 25분 소요

        return "crawlTest";
    }

    @GetMapping("/findTextLen/{id}")
    public String findTextLen(@PathVariable long id, Model model){

        int textLen=crawlService.findTextLen(id);

        model.addAttribute("textLen",textLen);
        return "crawlTest";
    }

}
