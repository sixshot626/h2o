package h2o.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Door {

    private static final class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -2765796120040071906L;

        boolean isSignalled() {
            return getState() != 0;
        }

        protected int tryAcquireShared(int ignore) {
            return isSignalled() ? 1 : -1;
        }

        public boolean tryReleaseShared(int releases) {
            setState(releases);
            return true;
        }
    }

    private final Sync sync;

    public Door() {
        this.sync = new Sync();
    }

    public Door(boolean open) {
        this.sync = new Sync();
        if (open) this.open();
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void open() {
        sync.releaseShared(1);
    }

    public void close() {
        sync.releaseShared(0);
    }

    public boolean getState() {
        return sync.isSignalled();
    }

    public String toString() {
        return super.toString() + "[Status = " + (getState() ? "opened" : "closed") + "]";
    }
}
