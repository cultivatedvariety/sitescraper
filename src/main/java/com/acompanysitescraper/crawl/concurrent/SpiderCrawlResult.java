package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.crawl.ParsedContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link CrawlResult} that uses the existence of contents to determine sucess
 */
public class SpiderCrawlResult implements CrawlResult {

    private final List<ParsedContent> contents;
    private final String url;
    private Exception exception;

    public SpiderCrawlResult(String url) {
        this.url = url;
        this.contents = new ArrayList<ParsedContent>(128);
    }

    public void addContent(ParsedContent parsedContent){
        this.contents.add(parsedContent);
    }

    @Override
    public List<ParsedContent> getContents() {
        return contents;
    }

    @Override
    public boolean wasSuccessful() {
        return this.contents.size() > 0;
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
