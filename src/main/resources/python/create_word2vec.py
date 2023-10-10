#W2V
from gensim.models import Word2Vec
from gensim.models import KeyedVectors
import pandas as pd

# noun_data.txt 파일에서 텍스트를 읽어와서 처리합니다.
noun_data_list = []

with open('noun_data.txt', 'r', encoding='utf-8') as file:
    for line in file:
        # 각 줄을 공백을 기준으로 단어로 분리하여 리스트에 추가합니다.
        words = line.strip().split()
        noun_data_list.append(words)

# 결과 출력
print(noun_data_list[:3])


model = Word2Vec(sentences=noun_data_list, vector_size=100, window=5, min_count=2, workers=4, sg=0)


model_result = model.wv.most_similar("세미나")
print(model_result)

#모델 csv형식으로 저장
df = pd.DataFrame(model.wv.vectors)
df.to_csv('./wv_model_tsv.tsv', sep = '\t', index = False)

#단어도 csv 형식으로 저장
word_df = pd.DataFrame(model.wv.index2word)
word_df.to_csv('./wv_word_tsv.tsv', sep = '\t', index = False)
