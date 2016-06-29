package com.acompanysitescraper.crawl;

import com.acompanysitescraper.crawl.concurrent.CrawlCallable;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;
import com.acompanysitescraper.crawl.concurrent.Executor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Main engine for crawling a set of URLs. Makes use of a spider to produce items and urls
 */
public class CrawlerEngine {

    private final Executor<SpiderCrawlResult> crawlExecutor;
    private final Spider spider;
    private final Object startStopLock;
    private boolean run;
    private final List<String> crawledUrls;
    private final Object crawledUrlsLock;
    private final ConcurrentHashMap<String, List<ParsedContent>> parsedContent;
    private Thread crawlResultsThread;

    public CrawlerEngine(Spider spider, Executor<SpiderCrawlResult> crawlExecutor) {
        if (spider == null){
            throw new IllegalArgumentException("spider cannot be null");
        }

        if (crawlExecutor == null){
            throw new IllegalArgumentException("crawlExecutor cannot be null");
        }

        this.spider = spider;
        this.crawlExecutor = crawlExecutor;
        this.startStopLock = new Object();
        this.crawledUrls = new ArrayList<String>(10000);
        this.crawledUrlsLock = new Object();
        this.parsedContent = new ConcurrentHashMap<String, List<ParsedContent>>();
    }

    public void start(){
        if (!this.run) {
            synchronized (this.startStopLock) {
                if (this.run) {
                    return; //already run
                }
                this.run = true;
            }
        }
        this.crawlResultsThread = new Thread(new Runnable() {
            public void run() {
                processCrawlResults();
            }
        });
        this.crawlResultsThread.start();
        //submit the seed url to get everything started
        crawlUrl(this.spider.getSeedUrl());
    }

    public void stop(){
        synchronized (this.startStopLock){
            if (!this.run){
                return; //nothing to stop
            }
        }
        this.crawlResultsThread.interrupt();
        this.crawlExecutor.shutdownNow();
        this.run = false;
    }

    public void crawlUrl(String url){
        if (!isAllowedDomain(url)){
            return;
        }
        if (this.crawledUrls.contains(url)){
            return; //already seen it
        } else {
            synchronized (this.crawledUrlsLock) {
                if (this.crawledUrls.contains(url)) {
                    return;
                }
                this.crawledUrls.add(url);
            }
        }
        this.crawlExecutor.submit(new CrawlCallable(url, this.spider, (newUrl) -> crawlUrl(newUrl)));
    }


    public ConcurrentHashMap<String, List<ParsedContent>> getParsedContent() {
        return parsedContent;
    }

    private void processCrawlResults(){
        try {
            while (!Thread.currentThread().interrupted()) {
                Future<SpiderCrawlResult> future = this.crawlExecutor.take();
                SpiderCrawlResult result = future.get();
                if (!result.wasSuccessful()){
                    //TODO: log
                    continue;
                }
                this.parsedContent.putIfAbsent(result.getUrl(), result.getContents());
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (Exception ex){
            //TODO: logging
        }
    }

    private boolean isAllowedDomain(String url){
        URI uri = null;
        try {
            uri = new URI(url);
            String domain = uri.getHost();
            domain = domain.startsWith("www.") ? domain.substring(4) : domain;
            for (String allowedDomain : this.spider.getAllowedDomains()){
                if (domain.toLowerCase().equals(allowedDomain))
                    return true;
            }
        } catch (URISyntaxException e) {
            //TODO: logging
        }
        return false;
    }
}
