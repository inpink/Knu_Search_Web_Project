package knusearch.clear.jpa.controller;


import java.util.List;
import knusearch.clear.elasticsearch.domain.BasePostElasticsearchEntity;
import knusearch.clear.elasticsearch.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final ElasticsearchService elasticsearchService;

    @GetMapping("/esOrTest/{query}")
    public String esOrTest(@PathVariable String query, Model model) {
        List<BasePostElasticsearchEntity> searchResult = elasticsearchService.searchOrPosts(query);
        log.info("esOrTest: " + searchResult);

        model.addAttribute("query", query);
        model.addAttribute("searchResult", searchResult);

        return "elasticsearchTest";
    }

    @GetMapping("/esAndTest/{query}")
    public String esAndTest(@PathVariable String query, Model model) {
        List<BasePostElasticsearchEntity> searchResult = elasticsearchService.searchAndPosts(query);
        log.info("esAndTest: " + searchResult);

        model.addAttribute("query", query);
        model.addAttribute("searchResult", searchResult);

        return "elasticsearchTest";
    }


}
