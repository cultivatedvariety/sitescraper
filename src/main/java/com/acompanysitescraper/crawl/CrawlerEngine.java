package com.acompanysitescraper.crawl;

import com.acompanysitescraper.Logger;
import com.acompanysitescraper.crawl.concurrent.CrawlCallable;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;
import com.acompanysitescraper.crawl.concurrent.Executor;
import com.acompanysitescraper.content.UrlContents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main engine for crawling a set of URLs. Makes use of a spider to produce items and urls
 */
public class CrawlerEngine {

    private final Executor<SpiderCrawlResult> crawlExecutor;
    private final Spider spider;
    private final Object startStopLock;
    private final Logger logger;
    private boolean run;
    private final List<String> crawledUrls;
    private final Object crawledUrlsLock;
    private final ConcurrentHashMap<String, UrlContents> parsedContent;
    private Thread crawlResultsThread;
    private final AtomicInteger totalCrawls;
    private final AtomicInteger completedCrawls;

    public CrawlerEngine(Spider spider, Executor<SpiderCrawlResult> crawlExecutor, Logger logger) {
        if (spider == null){
            throw new IllegalArgumentException("spider cannot be null");
        }

        if (crawlExecutor == null){
            throw new IllegalArgumentException("crawlExecutor cannot be null");
        }

        if (logger == null){
            throw new IllegalArgumentException("logger cannot be null");
        }

        this.spider = spider;
        this.crawlExecutor = crawlExecutor;
        this.logger = logger;
        this.startStopLock = new Object();
        this.crawledUrls = new ArrayList<String>(10000);
        this.crawledUrlsLock = new Object();
        this.parsedContent = new ConcurrentHashMap<String, UrlContents>();
        this.totalCrawls = new AtomicInteger(0);
        this.completedCrawls = new AtomicInteger(0);
    }

    public int getTotalCrawls(){
        return this.totalCrawls.intValue();
    }

    public int getCompletedCrawls(){
        return this.completedCrawls.intValue();
    }

    public int getInProgressCrawls(){
        return this.totalCrawls.intValue() - this.completedCrawls.intValue();
    }

    public boolean isFinishedCrawling(){
        return getInProgressCrawls() == 0;
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
        logger.logInfo("CrawlerEngine.start: started with seed url " + this.spider.getSeedUrl());
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
        this.totalCrawls.incrementAndGet();
        this.crawlExecutor.submit(new CrawlCallable(url, this.spider, (newUrl) -> crawlUrl(newUrl)));
        logger.logDebug("CrawlerEngine.crawlUrl: url submitted for crawling: " + url);
    }


    public Map<String, UrlContents> getParsedContents() {
        return parsedContent;
    }

    private void processCrawlResults(){
        try {
            while (this.run && !Thread.currentThread().interrupted()) {
                Future<SpiderCrawlResult> future = this.crawlExecutor.poll(5, TimeUnit.SECONDS);
                if (future == null) {
                    continue;
                }

                try {
                    SpiderCrawlResult result = future.get();
                    logger.logInfo("CrawlerEngine.processCrawlResults: Received result for " + result.getUrl() + ". wasSuccessful = " + result.wasSuccessful());
                    if (result.wasSuccessful()) {
                        this.parsedContent.putIfAbsent(result.getUrl(), result.getContents());
                    }
                } catch (Exception ex){
                    logger.logError("An error occurred processing a crawl result", ex);
                }
                this.completedCrawls.incrementAndGet();
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (Exception ex){
            logger.logError("An error occurred processing a crawl result. The result thread will terminate", ex);
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
            logger.logError("An error occurred checking if url " + url + " is an allowed domain", e);
        }
        return false;
    }
}
