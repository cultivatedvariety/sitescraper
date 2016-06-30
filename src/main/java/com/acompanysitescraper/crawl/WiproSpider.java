package com.acompanysitescraper.crawl;

import com.acompanysitescraper.content.SiteMapContentItem;
import com.acompanysitescraper.content.SiteMapUrlContent;
import com.acompanysitescraper.content.UrlContents;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implementation of {@link Spider} that for wiprodigital.com. Uses Jsoup under the hood as parsing html using
 * regex is pretty unreliable and there is no point re-inventing the wheel
 *
 * Only href links are currently extracted. Can be extended to extract image sources etc.
 */
public class WiproSpider implements Spider {

    private final String seedUrl;
    private final String[] allowedDomains;
    private final String[] urlExtensionsToIgnore;

    public WiproSpider() {
        allowedDomains = new String[]{"wiprodigital.com"};
        seedUrl = "http://wiprodigital.com/";
        urlExtensionsToIgnore = new String[] {".pdf", ".doc", ".xls"};
    }

    @Override
    public String getSeedUrl() {
        return this.seedUrl;
    }

    @Override
    public String[] getAllowedDomains() {
        return this.allowedDomains;
    }

    @Override
    public UrlContents parse(String url, String htmlContent, UrlFetchWriter urlFetchWriter) {
        SiteMapUrlContent urlContent = new SiteMapUrlContent(url);
        Document parsedHtml = Jsoup.parse(htmlContent);
        parseHrefUrls(url, urlFetchWriter, urlContent, parsedHtml);
        return urlContent;
    }

    private void parseHrefUrls(String url, UrlFetchWriter urlFetchWriter, SiteMapUrlContent urlContent, Document parsedHtml) {
        Elements links = parsedHtml.select("a[href]");
        for (Element link : links) {
            String parsedUrl = link.attr("href");
            if (parsedUrl == null){
                continue;
            }
            parsedUrl = parsedUrl.trim();
            if (parsedUrl == "" || parsedUrl.startsWith("#")){
                continue;
            }

            if (parsedUrl.contains(" ")) {
                parsedUrl = parsedUrl.replace(" ", "%20"); //make sure spaces are properly escaped
            }

            if (!isValidUrl(url)){
                continue;
            }

            urlContent.addContentItem(new SiteMapContentItem(url, parsedUrl));
            // check if the url ends in an extension to ignore before parsing
            for (String extension : urlExtensionsToIgnore) {
                if (parsedUrl.endsWith(extension)){
                    continue;
                }
            }
            if (parsedUrl.startsWith("#") || parsedUrl.startsWith("/#")){
                continue;
            }
            urlFetchWriter.enqueue(parsedUrl);
        }
    }

    private boolean isValidUrl(String url){
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
