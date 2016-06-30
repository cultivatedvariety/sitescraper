package com.acompanysitescraper.crawl;

import com.acompanysitescraper.content.UrlContents;
import com.acompanysitescraper.content.UrlContentItem;

/**
 * Responsible for parsing content and enqueuing any new urls for parsing
 */
public interface ContentParser {

    /**
     * Parses some scraped parse and generate a list of {@link UrlContentItem}
     *
     * @param url url that was scraped
     * @param content parse of the url
     * @param urlFetchWriter writer to write any URLs to fetch to
     * @return
     */
    UrlContents parse(String url, String content, UrlFetchWriter urlFetchWriter);
}
