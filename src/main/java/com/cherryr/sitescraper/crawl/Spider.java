package com.cherryr.sitescraper.crawl;

/**
 * Responsible for providing the {@link CrawlerEngine} with the urls to crawl and generating
 * parsed parse from the crawl
 */
public interface Spider extends ContentParser {

    /**
     * Returns the initial url to that the spider wants cralwed
     * @return
     */
    String getSeedUrl();

    /**
     * Returns an array of allowed domains that are allowed to be crawled
     * @return
     */
    String[] getAllowedDomains();
}
