package com.acompanysitescraper.crawl;

import java.util.List;

/**
 * Responsible for parsing content and enqueuing any new urls for parsing
 */
public interface ContentParser {
    /**
     * Parses some scraped parse and generate a list of {@link ParsedContent}
     *
     * @param url url that was scraped
     * @param content parse of the url
     * @param urlFetchWriter writer to write any URLs to fetch to
     * @return
     */
    List<ParsedContent> parse(String url, String content, UrlFetchWriter urlFetchWriter);
}
