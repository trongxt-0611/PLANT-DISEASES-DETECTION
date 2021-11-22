import flask
import werkzeug
import os
from flask import jsonify
from prediction import make_predict
from datetime import datetime
from mongodb import getByNameDisease
app = flask.Flask(__name__)


@app.route('/solution', methods = ['POST'])
def handle_request0():
    NameDisease_EN = flask.request.form["Name"]
    print(str(NameDisease_EN))
    res = getByNameDisease(NameDisease_EN)
    return flask.jsonify(Image1=str(res["Image1"]),
                          Image2 = str(res["Image2"]),
                          Image3=str(res["Image3"]),
                          Image4=str(res["Image4"]),
                          NameDisease_VN=str(res["NameDisease_VN"]),
                          NameDisease_ENG=str(res["NameDisease_ENG"]),
                          Howtocure=str(res["Howtocure"]),
                          Pathogens=str(res["Pathogens"]),
                          Symptom=str(res["Symptom"])
                          )

@app.route('/upload', methods = ['POST'])
def handle_request():
    imagefile = flask.request.files['image']
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image File name : " + imagefile.filename)
    # save image
    # destFolder = os.path.dirname(os.path.realpath(__file__))
    # path = os.path.join(destFolder, imagefile.filename)
    # imagefile.save(path)
    imagefile.save(filename)
    # predict 
    res = make_predict(filename)
    time = datetime.now().strftime("%d/%m/%Y %H:%M:%S")
  #  jsonCure = getByNameDisease(str(res["class"]))
    return flask.jsonify(response_class=str(res["class"]),
                         response_confident=str(res["confident"]),
                         response_time = time)

app.run(host="0.0.0.0", port=5000, debug=True)
