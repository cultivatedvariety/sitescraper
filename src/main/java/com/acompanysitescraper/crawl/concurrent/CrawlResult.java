package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.crawl.ParsedContent;

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
