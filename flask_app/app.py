from flask import Flask
from werkzeug.urls import url_quote
from werkzeug.urls import url_quote_plus as url_quote
app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello, welcome to my Flask app!'

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
