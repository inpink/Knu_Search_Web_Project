package knusearch.clear.jpa.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ContentController {

    @GetMapping("/contentResult")
    public String contentResult() {

        return "contentResult";
    }
}
