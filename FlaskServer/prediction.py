import matplotlib.pyplot as plt
import matplotlib.image as mpimg
# Import OpenCV
import cv2
import os
from tensorflow import keras
import numpy as np
dir = os.path.dirname(os.path.realpath(__file__))
IMAGE_SHAPE = (256, 256)
model = keras.models.load_model('my_model.h5')
classes = ['Pepper bell Bacterial_spot', 'Pepper bell healthy',
 'Potato Early blight' ,'Potato Late blight' ,'Potato healthy',
 'Tomato Bacterial spot', 'Tomato Early blight', 'Tomato Late blight',
 'Tomato Leaf Mold', 'Tomato Septoria leaf spot',
 'Tomato Spider mites Two spotted spider mite', 'Tomato Target Spot',
 'Tomato YellowLeaf Curl Virus', 'Tomato mosaic virus',
 'Tomato healthy']

def load_image(filename):
    img = cv2.imread(os.path.join(dir, filename))
    print(os.path.join(dir, filename))
    img = cv2.resize(img, (IMAGE_SHAPE[0], IMAGE_SHAPE[1]) )
    img = img /255
    return img


def predict(image):
    probabilities = model.predict(np.asarray([image]))[0]
    class_idx = np.argmax(probabilities)
    return {classes[class_idx]: probabilities[class_idx]}

def make_predict(filename):
    img = load_image(filename)
    prediction = predict(img)
    res = {
        "class": list(prediction.keys())[0],
        "confident":list(prediction.values())[0]
    }
    return res
    
