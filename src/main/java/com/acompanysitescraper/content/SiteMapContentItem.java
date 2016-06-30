package com.acompanysitescraper.content;

/**
 * Implementation of {@link ContentItem} that represents a site map entry
 */
public class SiteMapContentItem implements ContentItem {

    private String fieldUrl;
    private String fieldOutboundUrl;

    public SiteMapContentItem(String url, String outboundUrl) {
        this.fieldUrl = url;
        this.fieldOutboundUrl = outboundUrl;
    }

    public String getFieldUrl() {
        return fieldUrl;
    }

    public String getFieldOutboundUrl() {
        return fieldOutboundUrl;
    }

    @Override
    public String toString(){
        return fieldUrl + " -> " + fieldOutboundUrl;
    }
}
