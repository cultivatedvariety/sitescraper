package com.acompanysitescraper.sitemap;

import com.acompanysitescraper.Logger;
import com.acompanysitescraper.content.ContentItem;
import com.acompanysitescraper.content.UrlContents;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSinkSVG;
import org.graphstream.stream.file.FileSinkSVG2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link SiteMapRenderer} that renders {@link UrlContents} to
 * a dot file (https://en.wikipedia.org/wiki/DOT_(graph_description_language))
 */
public class DOTSiteMapRenderer implements SiteMapRenderer {

    public static final String FIELD_NAME_OUTBOUNDURL = "OutboundUrl";
    public static final String FIELD_NAME_URL = "Url";

    private final String siteDomain;
    private final String dotFilePath;
    private final Logger logger;

    private final Map<String, String> urlShortNameMap;

    public DOTSiteMapRenderer(String siteDomain, String dotFilePath, Logger logger) {
        this.siteDomain = siteDomain;
        this.dotFilePath = dotFilePath;
        this.logger = logger;
        urlShortNameMap = new HashMap<>();
    }

    @Override
    public void render(Map<String, UrlContents> urlContentsMap) throws Exception {
        Graph graph = new SingleGraph(siteDomain);
        for (UrlContents urlContents : urlContentsMap.values()){
            for (ContentItem contentItem : urlContents.getContents()){
                try {
                    String url = getUrlShortName(getContentItemUrl(contentItem));
                    String outboundUrl = getContentItemOutboundUrl(contentItem);
                    if (!isInSiteDomain(outboundUrl)){
                        outboundUrl = getDomain(outboundUrl);
                    }else{
                        outboundUrl = getUrlShortName(outboundUrl);
                    }

                    for (String nodeName : new String[] {url, outboundUrl}) {
                        if (graph.getNode(nodeName) == null) {
                            graph.addNode(nodeName);
                        }
                    }
                    String edgeName = outboundUrl;
                    if (graph.getEdge(edgeName) == null) {
                        graph.addEdge(edgeName, url, outboundUrl);
                    }
                } catch (Exception ex){
                    logger.logError("An error occurred rendering the ContentItem " + contentItem, ex);
                }
            }
        }
        FileSinkDOT fs = new FileSinkDOT();
        fs.writeAll(graph, dotFilePath);
    }

    private boolean isInSiteDomain(String url){
        String domain = getDomain(url);
        return domain.toLowerCase() == siteDomain.toLowerCase();
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

    private String getUrlShortName(String url){
//        if (!urlShortNameMap.containsKey(url)){
//            int lastSlash = url.lastIndexOf('/');
//            if (lastSlash >= 0 && lastSlash < url.length()){
//                urlShortNameMap.put(url, url.substring(lastSlash));
//            }else{
//                urlShortNameMap.put(url, url);
//            }
//        }
//        return urlShortNameMap.get(url);
        return url;
    }

    private String getContentItemUrl(ContentItem contentItem) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getFieldOutboundUrl = contentItem.getClass().getDeclaredMethod("getField" + FIELD_NAME_URL);
        String outboundUrl = (String) getFieldOutboundUrl.invoke(contentItem);
        return outboundUrl;
    }

    private String getContentItemOutboundUrl(ContentItem contentItem) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getFieldOutboundUrl = contentItem.getClass().getDeclaredMethod("getField" + FIELD_NAME_OUTBOUNDURL);
        String outboundUrl = (String) getFieldOutboundUrl.invoke(contentItem);
        return outboundUrl;
    }
}
