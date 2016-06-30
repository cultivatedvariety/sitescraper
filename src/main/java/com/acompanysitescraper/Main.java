package com.acompanysitescraper;

import com.acompanysitescraper.content.UrlContents;
import com.acompanysitescraper.crawl.*;
import com.acompanysitescraper.crawl.concurrent.Executor;
import com.acompanysitescraper.crawl.concurrent.FixedThreadExecutor;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;
import com.acompanysitescraper.sitemap.DOTSiteMapRenderer;
import com.acompanysitescraper.sitemap.SiteMapRenderer;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            Logger logger = new ConsoleLogger();

            Spider wiproSpider = new WiproSpider();
            Executor<SpiderCrawlResult> executor = new FixedThreadExecutor<SpiderCrawlResult>();
            CrawlerEngine crawlerEngine = new CrawlerEngine(wiproSpider, executor, logger);
            crawlerEngine.start();
            while (!crawlerEngine.isFinishedCrawling()) {
                Thread.sleep(5000);
            }
            crawlerEngine.stop();
            Map<String, UrlContents> urlContentsMap = crawlerEngine.getParsedContents();

            SiteMapRenderer siteMapRenderer = new DOTSiteMapRenderer(
                    wiproSpider.getAllowedDomains()[0],
                    ".\\wippro-sitemap.dot",
                    logger);
            siteMapRenderer.render(urlContentsMap);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("DONE");
    }
}
