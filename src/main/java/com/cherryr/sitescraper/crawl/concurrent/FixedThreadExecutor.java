package com.cherryr.sitescraper.crawl.concurrent;

import java.util.concurrent.*;

/**
 * Implementation of {@link com.cherryr.sitescraper.crawl.concurrent.Executor} that uses a fixed thread {@link ExecutorService} to
 * execute {@link Callable}s
 */
public class FixedThreadExecutor<R> implements com.cherryr.sitescraper.crawl.concurrent.Executor<R> {

    private final ExecutorService executorService;
    private final CompletionService<R> completionService;

    public FixedThreadExecutor() {
        this.executorService = Executors.newFixedThreadPool(2);
        this.completionService = new ExecutorCompletionService<R>(this.executorService);
    }

    public Future<R> submit(Callable<R> callable) {
        return this.completionService.submit(callable);
    }

    public Future<R> take() throws InterruptedException {
        return this.completionService.take();
    }

    public void shutdownNow() {
        this.executorService.shutdownNow();
    }
}
