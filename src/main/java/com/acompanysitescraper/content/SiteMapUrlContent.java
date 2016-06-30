package com.acompanysitescraper.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 30/06/2016.
 */
public class SiteMapUrlContent implements UrlContents {

    private final String url;
    private final List<SiteMapUrlContentItem> contents;

    public SiteMapUrlContent(String url) {
        this.url = url;
        contents = new ArrayList<SiteMapUrlContentItem>(500);
    }

    public void addContentItem(SiteMapUrlContentItem contentItem){
        contents.add(contentItem);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<SiteMapUrlContentItem> getContents() {
        return contents;
    }
}
