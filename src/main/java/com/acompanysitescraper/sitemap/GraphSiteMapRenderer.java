package com.acompanysitescraper.sitemap;

import com.acompanysitescraper.Logger;
import com.acompanysitescraper.content.UrlContents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Implementation of {@link SiteMapRenderer} that renders {@link UrlContents} as
 * a graph
 */
public class GraphSiteMapRenderer implements SiteMapRenderer {

    private final String siteDomain;
    private final Logger logger;

    public GraphSiteMapRenderer(String siteDomain, Logger logger) {
        this.siteDomain = siteDomain;
        this.logger = logger;
    }

    @Override
    public void render(Map<String, UrlContents> urlContentsMap) {
        

    }

    private String getDomain(String url){
        URI uri = null;
        try {
            uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            logger.logError("An error occurred getting the domain for url " + url, e);
        }
        return "";
    }
}
