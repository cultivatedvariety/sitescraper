package com.acompanysitescraper.content;

/**
 * Implementation of {@link UrlContentItem} that represents a site map entry
 */
public class SiteMapUrlContentItem implements UrlContentItem {

    private String url;
    private String outboundUrl;

    public SiteMapUrlContentItem(String url, String outboundUrl) {
        this.url = url;
        this.outboundUrl = outboundUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getOutboundUrl() {
        return outboundUrl;
    }
}
