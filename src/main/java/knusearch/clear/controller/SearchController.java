package knusearch.clear.controller;

import jakarta.validation.Valid;
import knusearch.clear.service.DateService;
import knusearch.clear.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final DateService dateService;
    private final SearchService searchService;

    @GetMapping("/search")
    public String searchForm(Model model){

        // searchForm에 기본값 담아서 반환할 것임
        SearchForm searchForm = new SearchForm();

        // YAML 파일에서 모든 site명을 가져와서 ArrayList에 담기
        List<String> selectedSites = searchService.findSites();
        String searchScopeRadio = searchService.findOrder();

        //기간 값 추가
        LocalDate searchPeriod_start = dateService.minDate();
        LocalDate searchPeriod_end = dateService.currentDate();


        searchForm.setSelectedSites(selectedSites);
        searchForm.setSearchPeriod_start(searchPeriod_start);  // value, min, max 값을 모델에 추가
        searchForm.setSearchPeriod_end(searchPeriod_end);
        searchForm.setSearchScopeRadio(searchScopeRadio);


        model.addAttribute("searchForm", searchForm);



        return "home";
    }


    /*
    @RequestParam("site") List<String> selectedSites,
                               @RequestParam("searchQuery") String searchQuery,
                               Model model
     */
    @GetMapping("/searchResult")
    public String searchResult(@Valid SearchForm searchForm, BindingResult result, Model model){

        //에러 처리
        if (searchForm.getSelectedSites().isEmpty()){
            result.rejectValue("selectedSites","required");
        }

        if (result.hasErrors()) {
            System.out.println("searchForm 검증 과정에서 에러 발생"+result.getAllErrors());
            return "home"; //앞에서 addError 다 해준 뒤 보내주자
        }


       /* if (searchForm.getSelectedSites() != null && !searchForm.getSelectedSites().isEmpty()) {

            //return "success"; // 처리 완료 페이지로 이동
        } else {
            System.out.println("선택된 사이트 없음");
            //return "error"; // 에러 페이지로 이동
        }


        if (searchForm.getSearchQuery() != null && !searchForm.getSearchQuery().isEmpty()) {
            // 검색어(searchText)를 처리
        } else {
            System.out.println("검색어가 입력되지 않았습니다.");
        }
*/

        //성공 로직
        // 선택된 사이트들의 값을 처리
        for (String site : searchForm.getSelectedSites()) {
            System.out.println("선택된 사이트: " + site);
        }
        System.out.println("검색어: " + searchForm.getSearchQuery());
        System.out.println("검색 정렬:"+searchForm.getSearchScopeRadio());
        System.out.println("검색 기간:"+searchForm.getSearchPeriodRadio());
        System.out.println("검색 기간 시작:"+searchForm.getSearchPeriod_start());
        System.out.println("검색 기간 끝:"+searchForm.getSearchPeriod_end());

        //객체 자체를 담아 보내줌! 타임리프에서 꺼내쓸 수 있다
        model.addAttribute("searchForm",searchForm);

        return "searchResult"; //redirect 말고 바로 page로 이동시킴. 이유는 아래에
    }

    //위에서 redirect해줬고, 아래 searchResult에서 html 타임리프로 값을 전달하고 싶으면
    //위에서 repository에 저장, 아래에서 repository에서 꺼내씀(service이용) 해야함


}
