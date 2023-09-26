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

}
