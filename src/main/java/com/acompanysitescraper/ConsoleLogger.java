package com.acompanysitescraper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by on 30/06/2016.
 */
public class ConsoleLogger implements Logger {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void logDebug(String message) {
        System.out.println(String.format("%s DEBUG: %s", sdf.format(new Date()), message));
    }

    @Override
    public void logInfo(String message) {
        System.out.println(String.format("%s INFO: %s", sdf.format(new Date()), message));
    }

    @Override
    public void logWarn(String message) {
        System.out.println(String.format("%s WARN: %s", sdf.format(new Date()), message));
    }

    @Override
    public void logError(String message) {
        System.out.println(String.format("%s ERROR: %s", sdf.format(new Date()), message));
    }

    @Override
    public void logError(String message, Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        System.out.println(String.format("%s ERR: %s %s %s", sdf.format(new Date()), message, ex.getMessage(), sw.toString()));
    }
}
