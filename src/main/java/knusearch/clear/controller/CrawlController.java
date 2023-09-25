package knusearch.clear.controller;

import knusearch.clear.domain.content.ContentMain;
import knusearch.clear.service.CrawlService;
import knusearch.clear.service.content.ContentMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlController {

    private final CrawlService crawlService;
    private final ContentMainService contentMainService;

    @GetMapping("/crawlTest")
    public void crawlTest() {

        //여기있는 내용은 나중에 ContentController나 CrawlController에 두면 될 듯
        String noticeBaseUrl="https://web.kangnam.ac.kr/menu/f19069e6134f8f8aa7f689a4a675e66f.do?paginationInfo.currentPageNo=";
        // 웹 페이지의 URL (아래는 공지사항 첫페이지. currentPageNo만 바뀌면 됨)
        String firsNoticetUrl = noticeBaseUrl+1;

        int totalPageIdx= crawlService.totalPageIdx(firsNoticetUrl);

           /* for (int i=1; i<=totalPageIdx; i++){
                System.out.println(i+"번째 페이지에 있는 모든 게시글 크롤링");
                crawlService.scrapeWebPage(noticeBaseUrl+i);
            }*/


        ContentMain contentMain = crawlService.scrapeWebPage(noticeBaseUrl+10); //10페이지에 있는 것 contentMain에 저장시킴?
        System.out.println("크롤링 확인:" + contentMain.getTitle()); //잘되고
        contentMainService.saveContentMain(contentMain); //저장 잘 됨.
        // 아직 일부(제목,본문)만 저장해서 다 저장할 수 있게 추가해야 하고,
        //crawlService.scrapeWebPage로는 10개 게시물 중 마지막 하나만 받아오니까 마지막 하나밖에 저장이 안됨..수정해야 함

        System.out.println("공지사항 게시판 - 크롤링 업데이트 완료!"); //2023-09-25기준 25분 소요

    }
}
