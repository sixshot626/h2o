package h2o.common.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public final class Locks {

    private Locks() {
    }

    public static Lock newLock() {
        return new java.util.concurrent.locks.ReentrantLock();
    }

    public static ReadWriteLock newReadWriteLock() {
        return new java.util.concurrent.locks.ReentrantReadWriteLock();
    }

    public static LockMap newLockMap() {
        return new LockMap();
    }

}
