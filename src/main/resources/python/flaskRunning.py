from tensorflow.keras.models import load_model
from tensorflow.keras.layers import MultiHeadAttention, LayerNormalization, Dense, Dropout
import tensorflow as tf
from gensim.models import Word2Vec
from flask import Flask, request, jsonify
import numpy as np
from tensorflow.keras.preprocessing.sequence import pad_sequences
from konlpy.tag import Okt # 형태소 분석기로 Okt를 사용합니다

class TransformerEncoderLayer(tf.keras.layers.Layer):
    def __init__(self, embed_dim, num_heads, ff_dim, rate=0.1):
        super(TransformerEncoderLayer, self).__init__()
        self.att = MultiHeadAttention(num_heads=num_heads, key_dim=embed_dim)
        self.ffn = tf.keras.Sequential([
            Dense(ff_dim, activation="relu"),
            Dense(embed_dim),
        ])
        self.layernorm1 = LayerNormalization(epsilon=1e-6)
        self.layernorm2 = LayerNormalization(epsilon=1e-6)
        self.dropout1 = Dropout(rate)
        self.dropout2 = Dropout(rate)

    def call(self, inputs, training=False):
        attn_output = self.att(inputs, inputs)
        attn_output = self.dropout1(attn_output, training=training)
        out1 = self.layernorm1(inputs + attn_output)
        ffn_output = self.ffn(out1)
        ffn_output = self.dropout2(ffn_output, training=training)
        return self.layernorm2(out1 + ffn_output)


# TensorFlow/Keras 모델과 Word2Vec 모델 불러오기
model =load_model("h5File_cnn_transformer_okt.h5", custom_objects={"TransformerEncoderLayer": TransformerEncoderLayer}, compile=False)
word2vec_model = Word2Vec.load('modelFile.model')

app = Flask(__name__)

ban_words = ["강남", "대학교", "까지", "기타","이내","학교"]
okt = Okt()

def input_model_test(input_text, word2vec_model, model): #.model  .h5
    filtered=wordsFilter(input_text)
    input_vectors = []
    words=[]

    for word in filtered:
        if word in word2vec_model.wv:
            words.append(word)
            vector = word2vec_model.wv[word]
            input_vectors.append(vector)
        else:
            input_vectors.append(np.zeros(word2vec_model.vector_size))

    input_vectors = pad_sequences([input_vectors], maxlen=100, dtype='float32')

    # 모델 예측
    predictions = model.predict(input_vectors)
    class_prediction = np.argmax(predictions, axis=-1)

    return (words,class_prediction[0])

def wordsFilter(text):
    tokens = okt.pos(text)

    return [token[0] for token in tokens
            if token[1] == 'Noun'
            and len(token[0]) > 1
            and token[0] not in ban_words
            and not any(char in "!@#$%^&*()_+{}[]|\\:;<>,.?/~" for char in token[0])]


@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json(force=True)
    input_text = data['text']
    words,class_prediction = input_model_test(input_text, word2vec_model, model)

    # Convert numpy.int64 to native Python int
    if isinstance(class_prediction, np.int64):
        class_prediction = int(class_prediction)

    return jsonify({'predicted_class': str(class_prediction), 'words':words})

if __name__ == '__main__':
    app.run(debug=True)
