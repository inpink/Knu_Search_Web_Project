package knusearch.clear.jpa.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClassificationService {

    private final RestTemplate restTemplate;

    @Transactional
    public String predictClassification(final String searchQuery){
        // Flask 서버에 요청을 보내기 위한 데이터 구성
        String flaskEndpoint = "http://127.0.0.1:5000/predict"; // Flask 서버의 URL
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", searchQuery);

        // Flask 서버로 POST 요청을 보내고 응답 받기
        ResponseEntity<String> response = restTemplate.postForEntity(flaskEndpoint, requestBody, String.class);
        String body = response.getBody();

        // 응답에서 분류값 추출
        JSONObject jsonResponse = new JSONObject(body);
        String predictedClass = String.valueOf(jsonResponse.getInt("predicted_class")); // "predicted_class" 키의 값을 int로 추출

        return predictedClass; // 추출된 분류값 반환
    }

}
