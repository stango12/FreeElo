import requests
import json
from flask import Flask
import flask
app = Flask(__name__)

#Expires 7/17/17
API_KEY = 'API HERE'

@app.route('/freeChamps')
def get_free_champs():
	ids = []
	api_call = requests.get('https://na.api.pvp.net/api/lol/na/v1.2/champion?freeToPlay=true&api_key=' + API_KEY).content
	champs = json.loads(api_call).get("champions")
	for i in range(len(champs)):
		ids.append(champs[i]["id"])
	print ' '.join(str(e) for e in ids)
	rsp_text = ' '.join(str(e) for e in ids)
	resp = flask.Response(rsp_text)
	resp.headers['Access-Control-Allow-Origin'] = '*'
	return resp

@app.route('/champion/<id>')
def get_champ_info(id):
	api_call = requests.get('https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/' + id + '?api_key=' + API_KEY).content
	resp = flask.Response(api_call)
	resp.headers['Access-Control-Allow-Origin'] = '*'
	return resp

@app.route('/rune/<id>')
def get_rune_info(id):
	api_call = requests.get('https://global.api.pvp.net/api/lol/static-data/na/v1.2/rune/' + id + '?runeData=image&api_key=' + API_KEY).content
	resp = flask.Response(api_call)
	resp.headers['Access-Control-Allow-Origin'] = '*'
	return resp
