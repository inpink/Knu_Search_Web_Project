package knusearch.clear.jpa.controller;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import knusearch.clear.jpa.domain.dto.SearchResult;
import knusearch.clear.jpa.service.ClassificationService;
import knusearch.clear.jpa.service.DateService;
import knusearch.clear.jpa.service.SearchService;
import knusearch.clear.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchController { //TODO:프론트, 백, AI 전반적으로 사용한 툴 모두 적고 모든 툴 사용한 이유 정리하여 작성, 발표

    private final DateService dateService;
    private final SearchService searchService;
    private final ClassificationService classificationService;

    @GetMapping("/search")
    public String searchForm(Model model) {

        // searchForm에 기본값 담아서 반환할 것임
        SearchForm searchForm = new SearchForm();

        // YAML 파일에서 모든 site명을 가져와서 ArrayList에 담기
        List<String> selectedSites = searchService.findSites();
        String searchScopeRadio = searchService.findOrder();

        //기간 값 추가
        String searchPeriodRadio = searchService.findPeriod();
        LocalDate searchPeriod_start = dateService.minDate();
        LocalDate searchPeriod_end = dateService.currentDate();

        searchForm.setSelectedSites(selectedSites);
        searchForm.setSearchPeriodRadio(searchPeriodRadio);
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
    public String searchResult(@Valid SearchForm searchForm, BindingResult result, Model model) {

        //에러 처리
        if (searchForm.getSelectedSites().isEmpty()) { //사이트 미선택 (List라 isEmpty)
            result.rejectValue("selectedSites", "required");
        }

        if (searchForm.getSearchScopeRadio() == null) { //정렬 순서 미선택 (String이라 null)
            result.rejectValue("searchScopeRadio", "required");
        }

        if (searchForm.getSearchPeriodRadio() == null) { //기간 미선택
            result.rejectValue("searchPeriodRadio", "required");
        }

        //ObjectError. date에 대해 필요하면 넣기.
        /*
        <div th:if="${#fields.hasGlobalErrors()}">
          <p class="field-error" th:each="err : ${#fields.globalErrors()}"
             th:text="${err}">전체 오류 메시지</p>
        </div>

        if (searchForm.getSearchScopeRadio().equals("directPeriodSelectChecked") &&
         searchForm.getSearchPeriod_start())
        {

        }*/

        // 매핑된것 출력해서 확인
        for (String site : searchForm.getSelectedSites()) {
            System.out.println("선택된 사이트: " + site);
        }
        System.out.println("검색어: " + searchForm.getSearchQuery());
        System.out.println("검색 정렬:" + searchForm.getSearchScopeRadio());
        System.out.println("검색 기간:" + searchForm.getSearchPeriodRadio());
        System.out.println("검색 기간 시작:" + searchForm.getSearchPeriod_start());
        System.out.println("검색 기간 끝:" + searchForm.getSearchPeriod_end());
        System.out.println("분류 추천 사용 여부:" + searchForm.getCategoryRecommendChecked());
        // 분류 메뉴 모델로부터 받아오기
        String predictedClass = classificationService.predictClassification(searchForm.getSearchQuery());
        String refinedPredictedClass = StringUtil.deleteLineSeparator(predictedClass);
        System.out.println("predictedClass = " + predictedClass);
        System.out.println("refinedPredictedClass = " + refinedPredictedClass);

        // 검색하기
        List<SearchResult> searchResult = searchResults(searchForm, refinedPredictedClass);
        model.addAttribute("searchResult", searchResult);

        //객체 자체를 담아 보내줌! 타임리프에서 꺼내쓸 수 있다
        model.addAttribute("searchForm", searchForm);

        if (result.hasErrors()) {
            System.out.println("searchForm 검증 과정에서 에러 발생" + result.getAllErrors());
            model.addAttribute("isSearchEnabled", false);
            return "searchResult"; //앞에서 addError 다 해준 뒤 보내주는 것
            //에러 뜨든 안뜨든 searchResult로 보냄. 거기서 다시 검색 옵션 선택하게 함.
        }

        // 분류값을 모델에 추가
        model.addAttribute("predictedClass", predictedClass);

        model.addAttribute("isSearchEnabled", true);
        return "searchResult"; //redirect 말고 바로 page로 이동시킴. 이유는 아래에
    }

    private List<SearchResult> searchResults(SearchForm searchForm, String refinedPredictedClass) {
        List<SearchResult> searchResult;

        if (searchForm.getCategoryRecommendChecked() == "categoryRecommendChecked") {
            searchResult = searchService.searchAndPostWithBoostClassification(
                    searchForm.getSearchQuery(), refinedPredictedClass); //검색어의 분류정보
        } else {
            searchResult = searchService.searchAndPosts(searchForm.getSearchQuery());
        }
        return searchResult;
    }

    //위에서 redirect해줬고, 아래 searchResult에서 html 타임리프로 값을 전달하고 싶으면
    //위에서 repository에 저장, 아래에서 repository에서 꺼내씀(service이용) 해야함
}
