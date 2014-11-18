__author__ = 'paufabregatpappaterra'

#utf-8
from geopy.geocoders import GoogleV3
import time

import urllib, urllib2
import json

start_time = time.time()
api_key = 'AIzaSyCy9TF6ktgmcMvGJ-vLq9vx0VVTq01wfmg'
address = 'Roger de Flor 229, Barcelona'

geolocator = GoogleV3(api_key=api_key)
location = geolocator.geocode(address)

print time.time() - start_time

start_time = time.time()
params = {'address': 'Nou de la Rambla 111 Barcelona',
          'sensor': 'false'}

url = 'http://maps.googleapis.com/maps/api/geocode/json?' + urllib.urlencode(params)

rawreply = urllib2.urlopen(url).read()
reply = json.loads(rawreply)

lat = reply['results'][0]['geometry']['location']['lat']
lng = reply['results'][0]['geometry']['location']['lng']


print  time.time() - start_time