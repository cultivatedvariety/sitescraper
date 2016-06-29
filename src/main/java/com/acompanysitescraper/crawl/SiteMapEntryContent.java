package com.acompanysitescraper.crawl;

/**
 * Implementation of {@link ParsedContent} that represents a site map entry
 */
public class SiteMapEntryContent implements ParsedContent {

    private String contentUrl;
    private String contentChildUrl;

    public SiteMapEntryContent(String contentUrl, String contentChildUrl) {
        this.contentUrl = contentUrl;
        this.contentChildUrl = contentChildUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getContentChildUrl() {
        return contentChildUrl;
    }
}
