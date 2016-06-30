package com.acompanysitescraper;

import com.acompanysitescraper.crawl.*;
import com.acompanysitescraper.crawl.concurrent.Executor;
import com.acompanysitescraper.crawl.concurrent.FixedThreadExecutor;
import com.acompanysitescraper.crawl.concurrent.SpiderCrawlResult;

public class Main {

    public static void main(String[] args) {
        try {
            Spider wiproSpider = new WiproSpider();
            Executor<SpiderCrawlResult> executor = new FixedThreadExecutor<SpiderCrawlResult>();
            Logger logger = new ConsoleLogger();
            CrawlerEngine crawlerEngine = new CrawlerEngine(wiproSpider, executor, logger);
            crawlerEngine.start();
            while (!crawlerEngine.isFinishedCrawling()) {
                Thread.sleep(5000);
            }
            crawlerEngine.stop();
            crawlerEngine.getParsedContent();
//            for (String url : crawlerEngine.getParsedContent().keySet()){
//                System.out.println(url);
//                for (UrlContentItem contentItem : crawlerEngine.getParsedContent().get(url)){
//                    System.out.println("\t" + ((SiteMapUrlContentItem) contentItem).getOutboundUrl());
//                }
//            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("DONE");
    }
}
