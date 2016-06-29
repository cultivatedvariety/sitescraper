# Overview
*sitescraper* is a very basic site scraping engine.

# Shortcommings
* Randomly generated parameters on urls may result in infinite parsing e.g. if http://mydom.com/index.html?q=1
& http://mydom.com/index.html?q=qqqp result in the same page but the *q* parameter changes every time then the page may be
parsed infinitely. A potential fix is generating a hashcode of the page and not-reparsing if the hashcode hasn't changed
* No logging

# Improvements
* Split CrawlerExecutor into two executors so that parsing and crawling are independent