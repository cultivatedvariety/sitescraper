package com.acompanysitescraper.sitemap;

import com.acompanysitescraper.content.UrlContents;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for rendering a sitemap from {@link UrlContents}
 */
public interface SiteMapRenderer {

    void render(Map<String, UrlContents> urlContentsMap);
}
