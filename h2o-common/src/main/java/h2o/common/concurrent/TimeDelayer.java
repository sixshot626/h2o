package h2o.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TimeDelayer {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();

    public boolean delay(long timeout) {
        if (timeout <= 0L) {
            return true;
        }
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeout);
        lock.lock();
        try {
            while (nanosRemaining > 0L) {
                nanosRemaining = available.awaitNanos(nanosRemaining);
            }
            return true;
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            lock.unlock();
        }
    }


    public void delayEx(long timeout) throws InterruptedException {
        if (timeout <= 0L) {
            return;
        }
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeout);
        lock.lock();
        try {
            while (nanosRemaining > 0L) {
                nanosRemaining = available.awaitNanos(nanosRemaining);
            }
        } finally {
            lock.unlock();
        }
    }


    public boolean delayUntil( long time ) {

        if (time < 0) {
            throw new IllegalArgumentException();
        }

        return delay( time - System.currentTimeMillis() );
    }

    public void delayUntilEx( long time ) throws InterruptedException {
        if (time < 0) {
            throw new IllegalArgumentException();
        }

        delayEx( time - System.currentTimeMillis() );
    }

}
