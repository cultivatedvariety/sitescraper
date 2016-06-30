package com.acompanysitescraper.content;

import java.util.List;

/**
 * Represents the content parsed from a url page
 */
public interface UrlContents {

    String getUrl();

    List<SiteMapUrlContentItem> getContents();
}
