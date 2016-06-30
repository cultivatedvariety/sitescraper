package com.acompanysitescraper.crawl.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for executing {@link Callable}s and providing access to its result
 */
public interface Executor<R> {

    Future<R> submit(Callable<R> callable);

    Future<R> take() throws InterruptedException;

    Future<R> poll(int timeout, TimeUnit timeUnit) throws InterruptedException;

    void shutdownNow();
}
