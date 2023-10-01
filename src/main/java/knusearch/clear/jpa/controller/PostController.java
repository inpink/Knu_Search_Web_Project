package knusearch.clear.jpa.controller;


import knusearch.clear.elasticsearch.domain.BasePostElasticsearchEntity;
import knusearch.clear.elasticsearch.service.ElasticsearchService;
import knusearch.clear.jpa.domain.post.BasePost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/esTest/{query}")
    public String esTest(@PathVariable String query, Model model) {
        List<BasePostElasticsearchEntity> searchResult = elasticsearchService.searchPosts(query);
        log.info("estest: " + searchResult);

        model.addAttribute("query", query);
        model.addAttribute("searchResult", searchResult);

        return "elasticsearchTest";
    }
}
