import elasticsearch
from konlpy.tag import Okt # 형태소 분석기로 Okt를 사용합니다

# Okt 형태소 분석기 초기화
okt = Okt()


print(elasticsearch.__version__) #버전 맞춰줘야함

# Elasticsearch 서버에 연결합니다.
es = elasticsearch.Elasticsearch([{'host': 'localhost', 'port': 9200}])

index_name = 'basepost-index'
query = {
    "query": {
        "match_all": {}  # 모든 문서를 검색합니다. 원하는 검색 쿼리를 여기에 추가할 수 있습니다.
    },
    "size": 100 #개수 지정헤줘야함 (default 10개)
}

# Elasticsearch에서 데이터를 가져옵니다.
response = es.search(index=index_name, body=query)

print("response type", type(response))

# 금칙어 리스트 정의
ban_words = ["강남", "대학교", "까지", "기타","이내","학교"]


# 검색 결과에서 "text" 필드를 추출하고 명사만 남깁니다.
noun_text_list = []
hit_count=0
for hit in response['hits']['hits']:
    hit_count+=1
    text = hit['_source']['text'] + hit['_source']['title']

     # 텍스트를 명사로 토큰화합니다.
    tokens = okt.pos(text)
    # 명사만 추출하여 공백으로 구분된 문자열로 변환합니다.
    #이 때, 금칙어를 뺍니다. 글자수가 1개인 명사도 뺍니다.
    #filtered_tokens = [token[0] for token in tokens if token[1] == 'Noun' and not any(char.isdigit() or char in "!@#$%^&*()_+{}[]|\\:;<>,.?/~") for char in token[0]]
    # 한 글자인 단어, 금칙어, 특수 문자를 제외하고 명사만 추출합니다.
    filtered_tokens = [token[0] for token in tokens
                       if token[1] == 'Noun'
                       and len(token[0]) > 1
                       and token[0] not in ban_words
                       and not any(char in "!@#$%^&*()_+{}[]|\\:;<>,.?/~" for char in token[0])]

    noun_text = " ".join(filtered_tokens)

    noun_text_list.append(noun_text)

print(hit_count)

for i in range(10):
    print(noun_text_list[i]+"★")


# 추출한 명사 데이터를 텍스트 파일에 저장합니다.
with open('noun_data.txt', 'w', encoding='utf-8') as file:
    file.write('\n'.join(noun_text_list))

print(f"{len(noun_text_list)} 명사 데이터가 'noun_data.txt' 파일에 저장되었습니다.")


