package com.acompanysitescraper.crawl.concurrent;

import java.util.concurrent.*;

/**
 * Implementation of {@link Executor} that uses a fixed thread {@link ExecutorService} to
 * execute {@link Callable}s
 */
public class FixedThreadExecutor<R> implements Executor<R> {

    private final ExecutorService executorService;
    private final CompletionService<R> completionService;

    public FixedThreadExecutor() {
        this.executorService = Executors.newFixedThreadPool(4);
        this.completionService = new ExecutorCompletionService<R>(this.executorService);
    }

    @Override
    public Future<R> submit(Callable<R> callable) {

        return this.completionService.submit(callable);
    }

    @Override
    public Future<R> take() throws InterruptedException {
        return this.completionService.take();
    }

    @Override
    public Future<R> poll(int timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.completionService.poll(timeout, timeUnit);
    }

    @Override
    public void shutdownNow() {
        this.executorService.shutdownNow();
    }
}
