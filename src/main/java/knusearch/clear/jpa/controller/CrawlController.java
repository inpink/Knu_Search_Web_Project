package knusearch.clear.jpa.controller;

import static knusearch.clear.constants.IntegerConstants.ACADEMIC_NOTIFICATION;
import static knusearch.clear.constants.IntegerConstants.EMPLOYMENT_STARTUP;
import static knusearch.clear.constants.IntegerConstants.LEARNING_KNOWHOW;
import static knusearch.clear.constants.IntegerConstants.SCHOLARSHIP;
import static knusearch.clear.constants.StringConstants.UNDETERMINED;

import java.util.ArrayList;
import java.util.List;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.domain.post.PostMain;
import knusearch.clear.jpa.service.post.BasePostService;
import knusearch.clear.jpa.service.post.PostIctService;
import knusearch.clear.jpa.service.post.PostMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlController {

    //하나의 controller에서 다른 service들 불러오는건 전혀 문제없음
    private final PostMainService postMainService;
    private final PostIctService postIctService;

    @GetMapping("/ictCrawlUpdate")
    public String ictCrawlUpdate() {

        postIctService.crawlUpdate();  //이 메소드 성공적으로 완료되는 시점에서 DB에 Commit됨(자세한 내용은 BasePostService참고)
        log.info("KNU ICT POST - 크롤링 업데이트 완료!");

        return "crawlTest";
    }

    @GetMapping("/mainCrawlUpdate")
    public String mainCrawlUpdate() {

        postMainService.crawlUpdate();
        log.info("KNU MAIN POST - 크롤링 업데이트 완료!");
        /*
        2023-09-25기준 25분 소요
        120게시물 담았을때 SHOW TABLE STATUS like 'content_main';로 확인해 봤을때
        Data_length가 98304=98,304 바이트=약 96 KB
         */

        return "crawlTest";
    }

    @GetMapping("/findTextLen/{id}")
    public String findTextLen(@PathVariable long id, Model model) {

        int textLen = postIctService.findPostTextLen(id);

        model.addAttribute("textLen", textLen);
        return "crawlTest";
    }

    @GetMapping("/classifyMain")
    public String classifyMain(Model model) {
        return classifyCommon(model, postMainService, "Main");
    }

    @GetMapping("/classifyIct")
    public String classifyIct(Model model) {
        return classifyCommon(model, postIctService, "Ict");
    }

    private String classifyCommon(Model model, BasePostService service, String postName) {
        List<BasePost> posts = service.findAll();
        model.addAttribute("postName", postName);
        model.addAttribute("posts", posts);

        List<String> classifications = determineClassOptions();
        model.addAttribute("classOptions", classifications);
        return "classify";
    }

    private static List<String> determineClassOptions() {
        List<String> classifications = new ArrayList<>();

        classifications.add(UNDETERMINED.getDescription());
        classifications.add(ACADEMIC_NOTIFICATION.toString());
        classifications.add(SCHOLARSHIP.toString());
        classifications.add(LEARNING_KNOWHOW.toString());
        classifications.add(EMPLOYMENT_STARTUP.toString());

        return classifications;
    }
}
