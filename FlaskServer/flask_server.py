import flask
import werkzeug
import os
from flask import jsonify
from prediction import make_predict
from datetime import datetime
app = flask.Flask(__name__)

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
    time = datetime.now().strftime("%d %m %Y %H %M %S")
    return flask.jsonify(response_class=str(res["class"]),
                         response_confident=str(res["confident"]),
                         response_time = time)

app.run(host="0.0.0.0", port=5000, debug=True)
