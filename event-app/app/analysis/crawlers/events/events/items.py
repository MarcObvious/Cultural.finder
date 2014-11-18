# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class EventsItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    description = scrapy.Field()
    title = scrapy.Field()
    price = scrapy.Field()
    date = scrapy.Field()
    time = scrapy.Field()
    category = scrapy.Field()
    latitude = scrapy.Field()
    longitude = scrapy.Field()
    venue = scrapy.Field()
