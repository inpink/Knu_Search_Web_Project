package knusearch.clear.controller;

import knusearch.clear.service.DateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final DateService dateService;

    @GetMapping("/")
    public String home(Model model) {
        log.info("home controller");

        String selectedDate = dateService.currentDate();
        String minDate = dateService.minDate();

        // value, min, max 값을 모델에 추가
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("minDate", minDate);

        return "home";
    }

    // PostMapping에서 파라미터로 xxxx-xx-xx형식으로 오는 데이터를 MemberDTO memberDTO에 받을 때,
    // MemberDTO의 field에  @DateTimeFormat(pattern = "yyyy-MM-dd") private Date birthDate;처럼
    //@DateTimeFormat을 달아서 원하는 형식에 맞게 복잡한 파싱 없이 Date 객체에 담아둘수 있음
    //https://velog.io/@chb1828/Controller%EC%97%90%EC%84%9C-Date-%EA%B0%9D%EC%B2%B4%EB%A5%BC-%EB%B0%9B%EB%8A%94-%EB%B0%A9%EB%B2%95 참고해보기
}
