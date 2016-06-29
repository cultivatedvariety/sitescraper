package com.acompanysitescraper;

import com.acompanysitescraper.crawl.CrawlerEngine;
import com.acompanysitescraper.crawl.Spider;
import com.acompanysitescraper.crawl.concurrent.CrawlCallable;
import com.acompanysitescraper.crawl.concurrent.Executor;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.concurrent.Future;

/**
 * Created by cultivated on 29/06/2016.
 */
public class CrawlerEngineTests {
    private Spider spider;
    private Executor<SpiderCrawlResult> executor;
    private Future<SpiderCrawlResult> future;
    private CrawlerEngine crawlerEngine;

    @Before
    public void Before(){
        spider = Mockito.mock(Spider.class);
        executor = (Executor< SpiderCrawlResult>)Mockito.mock(Executor.class);
        future = (Future<SpiderCrawlResult>)Mockito.mock(Future.class);
        crawlerEngine = new CrawlerEngine(spider, executor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_SpiderIsNull_Then_ExceptionThrown(){
        new CrawlerEngine(null, executor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void When_ExecutorIsNull_Then_ExceptionThrown(){
        new CrawlerEngine(spider, null);
    }

    @Test
    public void When_CrawlerStarted_Then_SeedUrlSubmittedForCrawling(){
        String seedUrl = "http://www.seedurl.com";
        String[] alllowedDomains = new String[]{"seedurl.com"};

        ArgumentCaptor<CrawlCallable> callableCaptor = ArgumentCaptor.forClass(CrawlCallable.class);
        Mockito.when(spider.getSeedUrl()).thenReturn(seedUrl);
        Mockito.when(spider.getAllowedDomains()).thenReturn(alllowedDomains);
        Mockito.when(executor.submit(callableCaptor.capture())).thenReturn(future);

        this.crawlerEngine.start();

        Mockito.verify(spider).getSeedUrl();
        Mockito.verify(executor).submit(Matchers.any(CrawlCallable.class));

        Assert.assertEquals(seedUrl, callableCaptor.getValue().getUrl());
    }

    @Test
    public void When_CrawlerStopped_Then_ExecutorStopped(){
        String seedUrl = "http://www.seedurl.com";
        String[] allowedDomains = new String[]{"seedurl.com"};

        Mockito.when(spider.getSeedUrl()).thenReturn(seedUrl);
        Mockito.when(spider.getAllowedDomains()).thenReturn(allowedDomains);

        this.crawlerEngine.start();
        this.crawlerEngine.stop();

        Mockito.verify(executor).shutdownNow();
    }

    @Test
    public void When_WhenUrlHasBeenCrawled_Then_ItIsNotCrawledAgain(){
        String url = "http://google.com";
        String[] allowedDomains = new String[]{"google.com"};

        ArgumentCaptor<CrawlCallable> callableCaptor = ArgumentCaptor.forClass(CrawlCallable.class);
        Mockito.when(executor.submit(callableCaptor.capture())).thenReturn(future);
        Mockito.when(spider.getAllowedDomains()).thenReturn(allowedDomains);


        this.crawlerEngine.crawlUrl(url);
        this.crawlerEngine.crawlUrl(url);

        Mockito.verify(executor, Mockito.times(1)).submit(Matchers.any());
    }

    @Test
    public void When_UrlIsNotAnAllowedDomain_Then_ItIsNotCrawled(){
        String url = "http://google.com";
        String[] allowedDomains = new String[]{"bing.com"};

        ArgumentCaptor<CrawlCallable> callableCaptor = ArgumentCaptor.forClass(CrawlCallable.class);
        Mockito.when(executor.submit(callableCaptor.capture())).thenReturn(future);
        Mockito.when(spider.getAllowedDomains()).thenReturn(allowedDomains);


        this.crawlerEngine.crawlUrl(url);

        Mockito.verify(executor, Mockito.times(0)).submit(Matchers.any());
    }
}
