package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.content.UrlContents;

/**
 * Implementation of {@link CrawlResult} that uses the existence of an exception to determine success
 */
public class SpiderCrawlResult implements CrawlResult {

    private UrlContents contents;
    private final String url;
    private Exception exception;

    public SpiderCrawlResult(String url) {
        this.url = url;
    }

    @Override
    public UrlContents getContents() {
        return contents;
    }

    public void setContents(UrlContents contents) {
        this.contents = contents;
    }

    @Override
    public boolean wasSuccessful() {
        return exception == null && contents != null;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
