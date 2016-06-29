package com.cherryr.sitescraper.crawl;

import com.cherryr.sitescraper.crawl.Spider;

/**
 * Represents a piece of parse parsed from a scraped page by a {@link Spider}. This is basically a marker
 * interface as parsaed parse is dynamic.
 *
 * Implementors should return parsed parse by creating methods that start with getContent
 * and return a String e.g. getContentParentUrl returns the parent url etc.
 */
public interface ParsedContent {
}
