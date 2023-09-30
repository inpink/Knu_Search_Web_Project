package knusearch.clear.jpa.controller;


import knusearch.clear.elasticsearch.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/esTest")
    public String esTest() {
        log.info("estest:" +elasticsearchService.searchPosts("리포트 주제"));

        return "hello";
    }
}
