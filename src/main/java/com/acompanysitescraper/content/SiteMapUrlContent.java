package com.acompanysitescraper.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 30/06/2016.
 */
public class SiteMapUrlContent implements UrlContents {

    private final String url;
    private final List<SiteMapContentItem> contents;

    public SiteMapUrlContent(String url) {
        this.url = url;
        contents = new ArrayList<SiteMapContentItem>(500);
    }

    public void addContentItem(SiteMapContentItem contentItem){
        contents.add(contentItem);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<SiteMapContentItem> getContents() {
        return contents;
    }
}
