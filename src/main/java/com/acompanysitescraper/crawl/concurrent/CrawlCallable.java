package com.acompanysitescraper.crawl.concurrent;

import com.acompanysitescraper.crawl.CrawlerEngine;
import com.acompanysitescraper.crawl.Spider;
import com.acompanysitescraper.crawl.UrlFetchWriter;
import com.acompanysitescraper.content.UrlContents;
import org.apache.commons.io.IOUtils;

import java.net.URI;
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
        String htmlContent = "";
        try {
            htmlContent = IOUtils.toString(new URI(this.url), "UTF-8");
            return getResult(htmlContent);
        } catch (Exception ex){
            SpiderCrawlResult errorResult = new SpiderCrawlResult(this.url);
            errorResult.setException(ex);
            return errorResult;
        }
    }

    public SpiderCrawlResult getResult(String htmlContent){
        SpiderCrawlResult result = new SpiderCrawlResult(this.url);
        try {
            UrlContents contents = this.spider.parse(this.url, htmlContent, this.urlFetchWriter);
            result.setContents(contents);
        } catch (Exception ex){
            result.setException(ex);
        }
        return result;
    }

    public String getUrl() {
        return url;
    }
}
