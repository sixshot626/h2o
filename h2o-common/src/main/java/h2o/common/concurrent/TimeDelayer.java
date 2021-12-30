package h2o.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TimeDelayer {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();

    public boolean delay( long timeout ) {
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeout);
        lock.lock();
        try {
            while (nanosRemaining > 500000L) {
                nanosRemaining = available.awaitNanos(nanosRemaining);
            }
            return true;
        } catch ( Exception e ) {
            return false;
        } finally {
            lock.unlock();
        }
    }


    public void delayTo( long time ) {
        if ( System.currentTimeMillis() >= time ) {
            return;
        }

        long delay = time - System.currentTimeMillis();
        while ( delay > 0 ) {
            if ( delay < 10000 && this.delay( delay >= 10000 ? 10000 : delay ) ) {
                return;
            }
            delay = time - System.currentTimeMillis();
        }
    }


}
