from tensorflow.keras.models import load_model
from gensim.models import Word2Vec
from flask import Flask, request, jsonify
import numpy as np
from tensorflow.keras.preprocessing.sequence import pad_sequences

# TensorFlow/Keras 모델과 Word2Vec 모델 불러오기
model = load_model('my_model.h5')
word2vec_model = Word2Vec.load('word2vec_model.model')

app = Flask(__name__)

def input_model_test(input_text, word2vec_model, model):
    # 입력 텍스트를 단어 리스트로 변환
    input_words = input_text.split()

    # Word2Vec 모델을 사용하여 단어를 벡터로 변환
    input_vectors = [word2vec_model.wv[word] for word in input_words if word in word2vec_model.wv]
    
    # 벡터가 비어 있으면, 영벡터로 처리
    if not input_vectors:
        input_vectors = [np.zeros(word2vec_model.vector_size)]

    # 패딩 처리
    max_sequence_length = 100  # 이전에 설정한 최대 시퀀스 길이
    input_vectors_pad = pad_sequences([input_vectors], maxlen=max_sequence_length, dtype='float32')

    # 모델 예측
    predictions = model.predict(input_vectors_pad)
    class_prediction = np.argmax(predictions, axis=-1)[0]

    return class_prediction

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json(force=True)
    input_text = data['text']
    class_prediction = input_model_test(input_text, word2vec_model, model)

    # Convert numpy.int64 to native Python int
    if isinstance(class_prediction, np.int64):
        class_prediction = int(class_prediction)

    return jsonify({'predicted_class': class_prediction})

if __name__ == '__main__':
    app.run(debug=True)
