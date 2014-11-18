#unicode = utf-8
__author__ = 'paufabregatpappaterra'

from scrapy.spider import Spider
from datetime import date
import datetime
import calendar
import urlparse
import urllib, urllib2
import json
import sys

from scrapy.selector import Selector
from scrapy.http import Request, Response
#from geopy.geocoders import GoogleV3

# Use Google RESTful API --> faster --- but I don't know if I need API key
#Alternative: geopy -- check test_geocoding.py
class Butxaca(Spider):
    count = 0
    nlinks = 0
    name = "butxaca"
    allowed_domains = ["butxaca.com"]
    start_urls = [
        "http://www.butxaca.com/"
    ]

    #[?][a-z]{4}[=][0-9]{4}-[0-9]{2}-[0-9]{2}
    def parse(self, response):


        now = date.today()

        daysMonth = calendar.monthrange(now.year, now.month)[1]

        datesArray = [datetime.date(now.year, now.month, day).isoformat() for day in range(now.day, daysMonth+1)]


        for dateString in datesArray:

            dateReq = '?date='+ dateString
            request = urlparse.urljoin(response.url, dateReq)

            yield Request(request, self.parseDate)


    def parseDate(self, response):
        xhs = Selector(response)

        links = xhs.xpath('//dd[contains(concat(" ", normalize-space(@id), " "), " panelDintre1 ")]//a[contains(concat(" ", normalize-space(@class), " "), " enllasEsdeveniment ")]')

        print '**************************************NUMERO DE ESDEVENIMENTS****************************************'

        self.nlinks = self.nlinks + len(links)
        print self.nlinks
        for eventLink in links:
            href = eventLink.xpath('@href').extract()[0]

            request = urlparse.urljoin(self.start_urls[0], href)
            yield Request(request, self.parseEvent)


    def parseEvent(self, response):
        xhs = Selector(response)

        self.getTitle(xhs)

        self.getDescription(xhs)

        self.getCategory(xhs)

        self.getPrice(xhs)

        self.getDistrict(xhs)

        self.getVenue(xhs)

        self.getAddress(xhs)

        self.getTime(xhs)

        self.count = self.count +1

        print 'CONTADOR ESDEVENIMENTS'
        print self.count


    def geocode(self, address):
        params = {'address': address,
                  'sensor': 'false'}

        url = 'http://maps.googleapis.com/maps/api/geocode/json?' + urllib.urlencode(params)

        raw_reply = urllib2.urlopen(url).read()
        reply = json.loads(raw_reply)

        try:
            lat = reply['results'][0]['geometry']['location']['lat']
            lng = reply['results'][0]['geometry']['location']['lng']

            return lat, lng

        except:
            print "Unexpected error:", sys.exc_info()[0]
            print reply

    def getTitle(self, xhs):
        title = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " capsalera-esdeveniment-columna ")]/h2/text()')

        if not title:
            title = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " grid_3 ")]/h2/text()')[0].extract()
        else:
            title = title[0].extract()

        title = title.strip()
        print title

    def getDescription(self, xhs):
        description = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " capsalera-esdeveniment-columna ")]/text()[preceding-sibling::h2][following-sibling::span]')

        descriptionItalic = xhs.xpath('//span[contains(concat(" ", normalize-space(@class), " "), " cursiva ")][preceding-sibling::h2][following-sibling::span]/text()')

        if not description: #handle Festival events format
            description = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " grid_3 ")]/text()[preceding-sibling::h2][following-sibling::span]')

            if isinstance(description[0].extract(), basestring):
                description = description[0].extract()
            else:
                description = ''
                print 'NO DESCRIPTION'

        elif isinstance(description[0].extract(), basestring): #There is no description in normal event
            description = description[0].extract()
        else:
            description = ''
            print 'NO DESCRIPTION'

        description = description.strip()

        if descriptionItalic:
            descriptionCursiva = descriptionItalic[0].extract()
            description = description + ' ' + descriptionCursiva
            description = description.strip()

        if description == '':
            print 'NO DESCRIPTION'
        else:
            print description

    def getCategory(self, xhs):
        category = xhs.xpath('//span[contains(concat(" ", normalize-space(@class), " "), " nomCategoria ")]/text()')[0].extract()
        category = category.strip()
        print category

    def getPrice(self, xhs):
        price = xhs.xpath('//span[contains(concat(" ", normalize-space(@class), " "), " texte-preu ")]/text()')[0].extract()
        price = price.strip()
        print price

    def getVenue(self, xhs):

        venue = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " capsalera-esdeveniment-columna ")]/a/text()')

        if not venue: #handle festival page format
            venue = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " primerElement ")]/a/text()')[0].extract()
        else:
            venue = venue[0].extract()
        venue = venue.strip()
        print venue

    def getDistrict(self, xhs):
        district_raw = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " grid_3 ")]/h3/text()')


        if len(district_raw) == 3: #handle when there is the div 'Informacio adicional'
            district_raw = district_raw[2].extract()
        else:
            district_raw = district_raw[1].extract()

        district = district_raw.split("Temps previst a",1)[1]
        district = district.strip()

        if district =='':
            print 'No district specified'
        else: print district

    def getAddress(self, xhs):
        address = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " grid_3 ")]/text()[following-sibling::img]')

        if not address:
            print 'No address'
        else:
            address = address[2].extract()
            address = address.encode('utf8').strip()
            address = address + ' Barcelona' #TODO Look up database to see if I already searched for that address if so get the lat and lng retrieved otherwise make new call to Google Maps API.
                                            # Save requests to Google Maps API 2500 per day 5 per sec
            print address
            #lat, lng = self.geocode(address)

            #print lat, lng

    def getTime(self, xhs):
        time_raw = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " capsalera-esdeveniment-columna ")]/b/text()')

        if not time_raw: #handle festival page format
            time_raw = xhs.xpath('//div[contains(concat(" ", normalize-space(@class), " "), " primerElement ")]/b/text()')[0].extract()

        else:
            time_raw = time_raw[0].extract()

        time = time_raw.replace(u'\xa0', u' ').split(' ')
        time = time[-1]
        time = time.strip()
        print time







