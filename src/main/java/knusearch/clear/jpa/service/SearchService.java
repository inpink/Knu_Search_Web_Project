package knusearch.clear.jpa.service;

/*
readOnly 속성은 해당 메서드에서 데이터베이스의 읽기 작업만 수행하고, 쓰기 작업은 하지 않음을 나타냅니다.
이렇게 설정된 메서드는 트랜잭션 커밋 시에 롤백되는 것을 방지하고, 데이터베이스에 대한 읽기 작업을 최적화할 수 있습니다.
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    //나중에는 repository(DAO)에서 가져올 듯
    public String findOrder(){
        return "InOrderChecked";
    }

    public String findPeriod() { return "allTimeChecked"; }

    public List<String> findSites(){

        List<String> sites = new ArrayList<>(){{
           add("unifiedSearchChecked");
           add("knuMainSiteChecked");
           add("knuIctSiteChecked");
           add("knuWelfareSiteChecked");
           add("knuSeniorSiteChecked");
           add("knuArtSiteChecked");
           add("knuClasSiteChecked");
           add("knuCtlSiteChecked");
        }};

        return sites;
    }

}
