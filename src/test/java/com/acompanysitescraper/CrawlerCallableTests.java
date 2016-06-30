package com.acompanysitescraper;

import com.acompanysitescraper.crawl.Spider;
import com.acompanysitescraper.crawl.UrlFetchWriter;
import com.acompanysitescraper.crawl.concurrent.CrawlCallable;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;
import com.acompanysitescraper.content.UrlContents;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Created by on 30/06/2016.
 */
public class CrawlerCallableTests {

    private Spider spider;
    private UrlFetchWriter urlFetchWriter;
    private UrlContents urlContents;
    private String url = "http://google.com";
    private CrawlCallable crawlCallable;

    @Before
    public void Before(){
        urlFetchWriter = Mockito.mock(UrlFetchWriter.class);
        spider = Mockito.mock(Spider.class);
        urlContents = Mockito.mock(UrlContents.class);
        crawlCallable = new CrawlCallable(url, spider, urlFetchWriter);
    }

    @Test
    public void When_CrawlableSuccessful_Then_ResultReturned(){
        Mockito.when(spider.parse(Matchers.anyString(), Matchers.anyString(), Matchers.any(UrlFetchWriter.class)))
                .thenReturn(urlContents);

        SpiderCrawlResult result = crawlCallable.getResult("");

        Assert.assertNotNull(result);
    }

    @Test
    public void When_CrawlableFails_Then_ResultReturned(){
        Mockito.when(spider.parse(Matchers.anyString(), Matchers.anyString(), Matchers.any(UrlFetchWriter.class)))
                .thenThrow(new RuntimeException("Failed"));

        SpiderCrawlResult result = crawlCallable.getResult("");

        Assert.assertNotNull(result);
    }

    @Test
    public void When_CrawlableFails_Then_ResultExceptionSet(){
        Mockito.when(spider.parse(Matchers.anyString(), Matchers.anyString(), Matchers.any(UrlFetchWriter.class)))
                .thenThrow(new RuntimeException("Failed"));

        SpiderCrawlResult result = crawlCallable.getResult("");

        Assert.assertNotNull(result.getException());
    }

    @Test
    public void When_SpiderReturnsContents_Then_ResultContainsContents(){
        String htmlContents = "Any contents";
        Mockito.when(spider.parse(url, htmlContents, urlFetchWriter)).thenReturn(urlContents);

        SpiderCrawlResult result = crawlCallable.getResult(htmlContents);
        Assert.assertNull(result.getException());
        Assert.assertNotNull(result.getContents());
        Assert.assertSame(urlContents, result.getContents());
    }

    @Test
    public void When_SpiderThrowsException_Then_ResultContainsExcption(){

    }

    @Test
    public void When_CallableInvokesSpider_Then_UrlAndContentsAndFetcherSet(){

    }
}
