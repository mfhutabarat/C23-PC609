import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import pickle
from flask import Flask, jsonify, request
from flask_ngrok import run_with_ngrok
import requests
from tensorflow.keras.preprocessing import image
import numpy as np

model = load_model('model3_binary.h5')

app = Flask(__name__)

def make_prediction(img):
    image_upl = image.load_img(img, target_size=(150,150))

    # Convert image to array
    image_upl = image.img_to_array(image_upl)

    # Rescale
    image_upl = image_upl / 255.0

    # Expand dimensions to match the expected input shape of the model
    image_upl = np.expand_dims(image_upl, axis=0)

    # Perform the prediction using the loaded model
    predictions = model.predict(image_upl)

    # Get the predicted class label
    predicted_class = np.argmax(predictions[0])

    return predicted_class

@app.route('/upload', methods=['POST'])
def upload():
    file = request.files['file']
    
    # Process the uploaded file, e.g., save it to disk
    file.save('uploaded_image.jpg')
 
    # Perform prediction using the loaded model
    # Assuming you have a function called `make_prediction` that takes the file path as input
    prediction = make_prediction('uploaded_image.jpg')

    # Convert the prediction result to a regular Python integer
    prediction = int(prediction)

    # Return the prediction result as JSON
    return jsonify({'result': prediction})

if __name__ == '__main__':
    app.run()