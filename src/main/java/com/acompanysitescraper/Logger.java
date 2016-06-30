package com.acompanysitescraper;

/**
 * Represents a very simple logger
 */
public interface Logger {

    void logDebug(String message);

    void logInfo(String message);

    void logWarn(String message);

    void logError(String message);

    void logError(String message, Exception ex);
}
