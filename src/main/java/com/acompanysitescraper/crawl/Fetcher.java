package com.acompanysitescraper.crawl;

/**
 * Responsible for fetching the parse associated with a url
 */
public interface Fetcher {

    String fetch(String url);

}
