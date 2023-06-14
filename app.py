import os
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import pickle
from flask import Flask, jsonify, request
from werkzeug.utils import secure_filename
from werkzeug.exceptions import RequestEntityTooLarge
from tensorflow.keras.preprocessing import image
import numpy as np
import urllib
from google.cloud import storage
from google.auth import default
import uuid
from PIL import Image

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = "capstone-c23-pc609-e96f0e327886.json"

credentials, project_id = default()
storage_client = storage.Client(credentials=credentials, project=project_id)

model = keras.models.load_model('model3_binary_categorical.h5')
#severity_mapping = {0: 'crack', 1: 'potholes'}

app = Flask(__name__)
UPLOAD_FOLDER = 'temp'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 10 * 1024 * 1024  # 10MB
if not os.path.exists(app.config['UPLOAD_FOLDER']):
    os.makedirs(app.config['UPLOAD_FOLDER'])

bucket_name = 'c23-pc609-bucket'  # Ganti dengan nama bucket Anda
bucket = storage_client.bucket(bucket_name)

ALLOWED_EXTENSIONS = {'jpg', 'jpeg', 'png', 'gif'}


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/upload', methods=['POST'])
def upload():
    try:
        try:
            content_length = request.content_length
            if content_length and content_length > app.config['MAX_CONTENT_LENGTH']:
                raise RequestEntityTooLarge('File melebihi ukuran maksimum yang diizinkan')
        except RequestEntityTooLarge as e:
            return jsonify({
                'status': 'error',
                'message': str(e)
            }), 413
        
        if 'image' not in request.files:
            return jsonify({
                'status': 'error',
                'message': 'Tidak ada bagian file dalam permintaan',
                'files_received': list(request.files.keys())
            }), 400

        file = request.files['image']

        if file.filename == '':
            return jsonify({
                'status': 'error',
                'message': 'Tidak ada file yang dipilih'
            }), 400

        if file and allowed_file(file.filename):
            if file.filename != '':
                unique_filename = f'{uuid.uuid4().hex}_{file.filename}'
                temp_path = os.path.join(app.config['UPLOAD_FOLDER'], unique_filename)
                file.save(temp_path)

                # Verifikasi gambar menggunakan PIL
                try:
                    image_upl = Image.open(temp_path)
                    image_upl.load()
                except (IOError, OSError) as e:
                    os.remove(temp_path)
                    return jsonify({
                        'status': 'error',
                        'message': 'File gambar tidak valid'
                    }), 400

                blob = bucket.blob(unique_filename, chunk_size=262144)
                blob.upload_from_filename(temp_path)
                image_url = f'https://storage.googleapis.com/{bucket.name}/{unique_filename}'
                image_upl = image.load_img(temp_path, target_size=(150, 150))
                image_upl = image.img_to_array(image_upl)
                image_upl = image_upl / 255.0
                image_upl = np.expand_dims(image_upl, axis=0)
                image_upl = np.vstack([image_upl])
                predictions = model.predict(image_upl)
                predicted_class = np.argmax(predictions)
                #severity_class = severity_mapping[predicted_class]
                os.remove(temp_path)

            return jsonify({
                'status': 'success',
                'message': 'File berhasil diunggah',
                'result': int(predicted_class),
                #'severity_class': severity_class,
            }), 200


        else:
            return jsonify({
                'status': 'error',
                'message': 'Jenis file tidak valid. Jenis file yang diizinkan: jpg, jpeg, png, gif'
            }), 400

    except Exception as e:
        return jsonify({
            'status': 'error',
            'message': str(e)
        }), 500


if __name__ == '__main__':
    app.run(debug=True, port=8080)
