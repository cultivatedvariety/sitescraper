package com.cherryr.sitescraper.crawl;

/**
 * Write a url to be fetched
 */
public interface UrlFetchWriter {

    void enqueue(String url);

}
