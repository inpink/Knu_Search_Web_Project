# KnuSearch [AI를 이용한 강대 홈페이지 통합 검색 성능 향상] 　   
이 작품은 2023 강남대학교 **졸업작품전시회**에서 **우수상**을 수여한 프로젝트입니다.

![K_Expo-전시회-포스터-양식_복사본-001 (4)](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/8b142fb2-2ab3-4f5f-a9d5-e421c8405529)


    
> 작품 소개

1. 강남대학교의 여러 홈페이지들의 게시글을 한 곳에서 검색할 수 있습니다. 
2. 사용자가 입력한 검색어와 관련도가 높은 순서대로 보여줍니다.
3. 사용자의 검색어가 어떤 분류에 해당하는지 파악하고, 같은 분류에 속하는 게시글을 더 관련도 높은 게시글로 볼 수 있습니다. 
4. 홈페이지 게시글 데이터의 사용을 위해 Crawling을 이용했으며, Spring Boot 환경에서 MySQL과 ElasticSearch Engine을 제어합니다.
5. 단어 검색과 검색 결과 정렬 설정은 ElasticSearch을 사용합니다.
6. AI(딥러닝, 자연어처리)를 통해 모든 게시글을 분류합니다.
  - Crawling한 데이터를 Word2Vec를 이용하여 단어들을 벡터화합니다.
  - 벡터화된 데이터는 CNN-LSTM 알고리즘을 거칩니다. CNN을 통해 문장에서 분류의 패턴에 해당될 수 있는 중요한 단어들을 파악하고, LSTM을 이용하여 중요 단어들을 길게 연결하며 문장의 문맥을 파악하여 최종적으로 분류합니다.

　   
> 사용 기술과 버전

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/32970ef8-c35d-41a8-be1e-ad9abc759819)

Spring boot: 3.1.3 　   
JPA: 3.1.3 　   
JDK: 17 　   
MySQL: 8.0.3 　   
Ubuntu: 22.04 　   
Docker: 24.0.6 　   
Elasticsearch: 8.7.1 　   
Morpheme Analyzer: nori 　   
Kibana: 8.7.1 　   
Logstash: 7.17.13 　   
Spring Data Elasticsearch: 5.1.3 　   
 　   
jQuery: 3.6.0 　   
Bootstrap: 5.0.2 　   
Thymeleaf: 3.1.2 　   
 　   
AI Algorithm: Word2Vec, CNN-LSTM 　   
 　   
 　   

> CNN-LSTM을 이용한 메뉴 분류

**[ 메뉴 분류 ]**　   
"학사":0, "장학":1, "학습/상담":2, "취창업":3, "교내":4, "교외":4

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/649baa99-f0a3-4fcc-91a2-1e3c85f83824)
`val_loss: 0.4312 - val_accuracy: 0.8709`

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/22c19ecf-a3c6-4ec4-b45b-c953abea37f7)

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/669f7e54-3c30-4e3e-b6df-00a264b6e867)

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/368dcd62-452b-41d1-b0e5-6cee08522b09)

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/1c87a55b-dae6-43f6-bb99-e2de1a1febef)

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/70ffea88-2634-4c37-aad4-9130e468de95)

![image](https://github.com/inpink/Knu_Search_Web_Project/assets/108166692/8a5bcd00-0001-43f2-8284-2c946945bb8a)


- 참고할만한 내용 포스팅한 링크 올릴 예정!
