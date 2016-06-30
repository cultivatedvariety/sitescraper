package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.content.UrlContents;

/**
 * Represents the result of a crawl
 *
 */
public interface CrawlResult {

    UrlContents getContents();

    boolean wasSuccessful();

    String getUrl();

    Exception getException();
}
