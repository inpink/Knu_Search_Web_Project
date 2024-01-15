package knusearch.clear.jpa.controller;

import static knusearch.clear.constants.StringConstants.UNDETERMINED;
import static knusearch.clear.jpa.domain.Classification.ACADEMIC_NOTIFICATION;
import static knusearch.clear.jpa.domain.Classification.EMPLOYMENT_STARTUP;
import static knusearch.clear.jpa.domain.Classification.LEARNING_KNOWHOW;
import static knusearch.clear.jpa.domain.Classification.SCHOLARSHIP;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import knusearch.clear.jpa.domain.Classification;
import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.domain.post.ClassificationUpdateRequest;
import knusearch.clear.jpa.service.post.BasePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlController {

    //하나의 controller에서 다른 service들 불러오는건 전혀 문제없음
    private final BasePostService basePostService;
    @GetMapping("/testRepo")
    public String testRepo() throws SQLException {
        BasePost basePost = new BasePost();
        basePost.setClassification("1");
        basePostService.savePost(basePost);
        return "hello";
    }

    @GetMapping("/ictCrawlUpdate")
    public String ictCrawlUpdate() {

        basePostService.crawlUpdate();  //이 메소드 성공적으로 완료되는 시점에서 DB에 Commit됨(자세한 내용은 BasePostService참고)
        log.info("KNU ICT POST - 크롤링 업데이트 완료!");

        return "crawlTest";
    }

    @GetMapping("/mainCrawlUpdate")
    public String mainCrawlUpdate() {

        basePostService.crawlUpdate();
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
        int textLen = basePostService.findPostTextLen(id);

        model.addAttribute("textLen", textLen);
        return "crawlTest";
    }

    @GetMapping("/classifyMain")
    public String classifyMain(Model model) {
        List<BasePost> posts = basePostService.findAll();
        model.addAttribute("postName", "NONE");
        model.addAttribute("posts", posts);

        List<String> classifications = determineClassOptions();
        model.addAttribute("classOptions", classifications);
        return "classify";
    }

    private static List<String> determineClassOptions() {
        List<String> classifications = new ArrayList<>();

        classifications.add(UNDETERMINED.getDescription());
        classifications.add(ACADEMIC_NOTIFICATION.getDescription());
        classifications.add(SCHOLARSHIP.getDescription());
        classifications.add(LEARNING_KNOWHOW.getDescription());
        classifications.add(EMPLOYMENT_STARTUP.getDescription());

        return classifications;
    }

    @PostMapping("/api/updateClassification")
    public ResponseEntity<?> updateClassification(@RequestBody ClassificationUpdateRequest request) {
        System.out.println("updateClassification called" + request.getPostId());
        Optional<BasePost> basePost = basePostService.findById(request.getPostId());

        if (basePost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID의 게시물이 존재하지 않습니다.");
        }

        basePostService.updateClassification(
                basePost.get(),
                transformClassification(request.getClassification()));
        return ResponseEntity.ok().build();
    }

    private String transformClassification(final String description) {
        return String.valueOf(Classification.findIndex(description));
    }
}
