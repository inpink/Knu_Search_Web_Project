package knusearch.clear.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    @GetMapping("/search")
    public String searchForm(Model model){
        model.addAttribute("searchForm", new SearchForm());
        return "home";
    }

    /*
    @RequestParam("site") List<String> selectedSites,
                               @RequestParam("searchQuery") String searchQuery,
                               Model model
     */
    @PostMapping("/search")
    public String search(@Valid SearchForm searchForm, BindingResult result, Model model){

        if (result.hasErrors()) {
            return "home";
        }

        if (searchForm.getSelectedSites() != null && !searchForm.getSelectedSites().isEmpty()) {
            // 선택된 사이트들의 값을 처리
            for (String site : searchForm.getSelectedSites()) {
                // 여기에서 선택된 사이트 값(site)에 대한 작업 수행
                System.out.println("선택된 사이트: " + site);
            }
            //return "success"; // 처리 완료 페이지로 이동
        } else {
            System.out.println("선택된 사이트 없음");
            //return "error"; // 에러 페이지로 이동
        }


        if (searchForm.getSearchQuery() != null && !searchForm.getSearchQuery().isEmpty()) {
            // 검색어(searchText)를 처리
            System.out.println("검색어: " + searchForm.getSearchQuery());
            model.addAttribute("searchQuery",searchForm.getSearchQuery());
        } else {
            System.out.println("검색어가 입력되지 않았습니다.");
        }


        return "searchResult"; //redirect 말고 바로 page로 이동시킴. 이유는 아래에
    }

    //위에서 redirect해줬고, 아래 searchResult에서 html 타임리프로 값을 전달하고 싶으면
    //위에서 repository에 저장, 아래에서 repository에서 꺼내씀(service이용) 해야함


}