package com.acompanysitescraper.crawl.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Responsible for executing {@link Callable}s and providing access to its result
 */
public interface Executor<R> {

    Future<R> submit(Callable<R> callable);

    Future<R> take() throws InterruptedException;

    void shutdownNow();
}
