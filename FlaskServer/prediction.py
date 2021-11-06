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
classes = ['Pepper_bell_Bacterial_spot', 'Pepper_bell_healthy',
 'Potato_Early_blight' ,'Potato_Late_blight' ,'Potato_healthy',
 'Tomato_Bacterial_spot', 'Tomato_Early_blight', 'Tomato_Late_blight',
 'Tomato_Leaf_Mold', 'Tomato_Septoria_leaf_spot',
 'Tomato_Spider_mites_Two_spotted_spider_mite', 'Tomato_Target_Spot',
 'Tomato_Tomato_YellowLeaf_Curl_Virus', 'Tomato_Tomato_mosaic_virus',
 'Tomato_healthy']

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
    
