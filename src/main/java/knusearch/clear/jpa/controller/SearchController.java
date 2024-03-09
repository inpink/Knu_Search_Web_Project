package knusearch.clear.jpa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import knusearch.clear.jpa.domain.post.BasePost;
import knusearch.clear.jpa.service.ClassificationService;
import knusearch.clear.jpa.service.DateService;
import knusearch.clear.jpa.service.SearchService;
import knusearch.clear.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        searchForm.setCategoryRecommendChecked("categoryRecommendChecked");

        model.addAttribute("searchForm", searchForm);

        return "home";
    }

    @GetMapping("/searchResult")
    public String searchResult(@Valid SearchForm searchForm,
                               BindingResult result,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model,
                               HttpServletRequest request) {
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
        Pageable pageable = PageRequest.of(page, size);
        Page<BasePost> searchResult = searchResults(
                searchForm, refinedPredictedClass,
                page, size, model);
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

        // 끝까지 왔을 때 검색 가능
        model.addAttribute("isSearchEnabled", true);
        return "searchResult"; //redirect 말고 바로 page로 이동시킴. 이유는 아래에
    }

    private Page<BasePost> searchResults(SearchForm searchForm,
                                         String refinedPredictedClass,
                                         int page,
                                         int size,
                                         Model model
    ) {
        List<Map.Entry<BasePost, Integer>> searchResultWithCount;

        if (searchForm.getCategoryRecommendChecked() == "categoryRecommendChecked") {
            searchResultWithCount = searchService.searchAndPostWithBoostClassification(
                    searchForm.getSearchQuery(), refinedPredictedClass); //검색어의 분류정보
        } else {
            searchResultWithCount = searchService.searchAndPosts(searchForm.getSearchQuery());
        }
        // count개수 담은 basepost map 보내기
        model.addAttribute("searchResultWithCount", searchResultWithCount);
        System.out.println("searchResultWithCount = " + searchResultWithCount);

        // basepost만 따로 리스트로 추출하여 페이지로 보내기
        List<BasePost> basePosts = searchResultWithCount.stream()
                .map(Map.Entry::getKey)
                .toList();

        return listToPage(basePosts, page, size);
    }

    private Page<BasePost> listToPage(List<BasePost> list, int page, int size) {
        // 시작 인덱스 계산
        int start = Math.min(page * size, list.size());
        // 종료 인덱스 계산
        int end = Math.min((start + size), list.size());
        // 서브리스트 생성
        List<BasePost> subList = list.subList(start, end);
        // PageRequest 객체 생성, 페이지 번호는 0부터 시작하므로 1을 빼줘야 한다는 점에 유의
        PageRequest pageRequest = PageRequest.of(page, size);
        // PageImpl 객체 생성 및 반환
        return new PageImpl<>(subList, pageRequest, list.size());
    }

    //위에서 redirect해줬고, 아래 searchResult에서 html 타임리프로 값을 전달하고 싶으면
    //위에서 repository에 저장, 아래에서 repository에서 꺼내씀(service이용) 해야함
}
