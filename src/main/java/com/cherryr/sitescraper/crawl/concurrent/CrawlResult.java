package com.cherryr.sitescraper.crawl.concurrent;

import com.cherryr.sitescraper.crawl.ParsedContent;

import java.util.List;

/**
 * Represents the result of a crawl
 *
 */
public interface CrawlResult {

    List<ParsedContent> getContents();

    boolean wasSuccessful();

    String getUrl();

    Exception getException();
}
