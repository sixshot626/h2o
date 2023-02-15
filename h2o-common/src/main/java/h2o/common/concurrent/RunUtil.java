package h2o.common.concurrent;

import h2o.common.concurrent.executor.Executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class RunUtil {

    private final ExecutorService executorService;
    private final AtomicInteger count;
    private final boolean autoShutdown;

    public RunUtil( String name ) {

        count = null;
        autoShutdown = false;

        this.executorService = Executors.newSingleThreadExecutor(name);
    }

    public RunUtil( int n, boolean autoShutdown ,  String name ) {

        this.autoShutdown = autoShutdown;
        if (autoShutdown) {
            count = new AtomicInteger(n);
        } else {
            count = null;
        }

        this.executorService = Executors.newFixedThreadPool(n,name);
    }

    public RunUtil( int n, boolean autoShutdown ,  ExecutorService es ) {

        this.autoShutdown = autoShutdown;
        if (autoShutdown) {
            count = new AtomicInteger(n);
        } else {
            count = null;
        }

        this.executorService = es;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public <T> Future<T> submit(Callable<T> task) {

        Future<T> f = executorService.submit(task);

        this.shutdownAuto();

        return f;

    }

    public Future<?> submit(Runnable task) {

        Future<?> f = executorService.submit(task);

        this.shutdownAuto();

        return f;
    }


    private void shutdownAuto() {
        if (this.autoShutdown) {
            if (count.decrementAndGet() == 0) {
                executorService.shutdown();
            }
        }

    }

    public void shutdown() {
        executorService.shutdown();
    }


    public static <T> Future<T> call(Callable<T> task, String name ) {

        RunUtil runUtil = new RunUtil(name);

        Future<T> f = runUtil.submit(task);
        runUtil.shutdown();

        return f;
    }

    @SuppressWarnings("unchecked")
    public static <T> Future<T>[] call(Callable<T> task, int n, String name) {

        if (n < 1) {
            return new Future[0];
        } else if (n == 1) {
            return new Future[]{call(task,name)};
        }

        RunUtil runUtil = new RunUtil(n, true , name);

        Future<T>[] fs = new Future[n];

        for (int i = 0; i < n; i++) {
            fs[i] = runUtil.submit(task);
        }

        return fs;
    }


    public static Future<?> run(Runnable task, String name) {

        RunUtil runUtil = new RunUtil(name);

        Future<?> f = runUtil.submit(task);
        runUtil.shutdown();

        return f;
    }


    public static Future<?>[] run(Runnable task, int n, String name) {

        if (n < 1) {
            return new Future[0];
        } else if (n == 1) {
            return new Future[]{run(task , name)};
        }

        RunUtil runUtil = new RunUtil( n, true , name );

        Future<?>[] fs = new Future[n];

        for (int i = 0; i < n; i++) {
            fs[i] = runUtil.submit(task);
        }

        return fs;
    }


    /**
     * @param millis
     * @return interrupted
     */
    public static boolean sleep(long millis) {

        try {
            Thread.sleep(millis);
            return false;
        } catch (InterruptedException e) {
            return true;
        }

    }


    public static boolean delay(long timeout) {
        return new TimeDelayer().delay(timeout);
    }

    public static void delayEx(long timeout) throws InterruptedException {
        new TimeDelayer().delayEx(timeout);
    }

    public static boolean delayUntil(long time) {
        return new TimeDelayer().delayUntil(time);
    }

    public static void delayUntilEx(long timeout) throws InterruptedException {
        new TimeDelayer().delayUntilEx(timeout);
    }

}
