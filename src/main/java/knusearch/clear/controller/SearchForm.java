package knusearch.clear.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchForm { //form 데이터를 bindingResult로 받아올때 쓸 객체

    @NotEmpty(message = "하나 이상의 검색 사이트를 선택해주세요.")
    private List<String> selectedSites;

    private String searchQuery;


}