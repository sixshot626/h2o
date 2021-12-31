package h2o.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TimeDelayer {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();

    public boolean delay( long timeout ) {
        if ( timeout <= 0L ) {
            return true;
        }
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeout);
        lock.lock();
        try {
            while (nanosRemaining > 0L) {
                nanosRemaining = available.awaitNanos(nanosRemaining);
            }
            return true;
        } catch ( Exception e ) {
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean delayUntil( long time ) {
        return delayUntil( time , false );
    }

    public void uninterruptedDelayUntil( long time ) {
        delayUntil( time , true );
    }

    private boolean delayUntil( long time , boolean interruptible ) {

        if ( time < 0 ) {
            throw new IllegalArgumentException();
        }

        long delayTime = time - System.currentTimeMillis();
        while ( delayTime > 0 ) {
            if (this.delay( delayTime >= 10000L ? 10000L : delayTime )) {
                if ( delayTime < 10000L ) {
                    return true;
                }
            } else if (interruptible) {
                return System.currentTimeMillis() >= time;
            }
            delayTime = time - System.currentTimeMillis();
        }

        return true;
    }


}
