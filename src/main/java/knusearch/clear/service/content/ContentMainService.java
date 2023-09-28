package knusearch.clear.service.content;

import knusearch.clear.domain.content.BaseContent;
import knusearch.clear.domain.content.ContentMain;
import knusearch.clear.repository.content.ContentMainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentMainService {

    private final ContentMainRepository contentMainRepository;

    public void saveContentMain(BaseContent contentMain) {
        contentMainRepository.save(contentMain);
    }

    public String[] getAllBaseUrl(){
        return new String[] {"https://web.kangnam.ac.kr/menu/f19069e6134f8f8aa7f689a4a675e66f.do?paginationInfo.currentPageNo=",
                "https://web.kangnam.ac.kr/menu/e4058249224f49ab163131ce104214fb.do?paginationInfo.currentPageNo="};
        //공지사항,  행사/안내 등

    }
}
