package knusearch.clear.controller;

import knusearch.clear.domain.post.BasePost;
import knusearch.clear.service.CrawlService;
import knusearch.clear.service.post.PostIctService;
import knusearch.clear.service.post.PostMainService;
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
    private final PostMainService postMainService;
    private final PostIctService postIctService;

    @GetMapping("/ictCrawlUpdate")
    public String ictCrawlUpdate(){
        String[] allIctBaseUrl = postIctService.getAllBaseUrl();

        crawlService.update(allIctBaseUrl);
        log.info("KNU ICT POST - 크롤링 업데이트 완료!");

        return "crawlTest";
    }

    @GetMapping("/mainCrawlUpdate")
    public String mainCrawlUpdate() {
        String[] allMainBaseUrl = postMainService.getAllBaseUrl();

        crawlService.update(allMainBaseUrl);
        log.info("KNU MAIN POST - 크롤링 업데이트 완료!");
        /*
        2023-09-25기준 25분 소요
        120게시물 담았을때 SHOW TABLE STATUS like 'content_main';로 확인해 봤을때
        Data_length가 98304=98,304 바이트=약 96 KB
         */

        return "crawlTest";
    }

    @GetMapping("/findTextLen/{id}")
    public String findTextLen(@PathVariable long id, Model model){

        int textLen=crawlService.findTextLen(id);

        model.addAttribute("textLen",textLen);
        return "crawlTest";
    }

}
