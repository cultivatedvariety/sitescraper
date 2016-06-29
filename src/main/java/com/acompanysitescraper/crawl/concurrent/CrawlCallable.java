package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.crawl.CrawlerEngine;
import com.acompanysitescraper.crawl.Spider;
import com.acompanysitescraper.crawl.UrlFetchWriter;

import java.util.concurrent.Callable;

/**
 * {@link Callable} used by {@link CrawlerEngine} to crawl
 */
public class CrawlCallable implements Callable<SpiderCrawlResult> {

    private final String url;
    private final Spider spider;
    private final UrlFetchWriter urlFetchWriter;

    public CrawlCallable(String url, Spider spider, UrlFetchWriter urlFetchWriter) {
        this.url = url;
        this.spider = spider;
        this.urlFetchWriter = urlFetchWriter;
    }

    public SpiderCrawlResult call() throws Exception {
        return null;
    }

    public String getUrl() {
        return url;
    }
}
