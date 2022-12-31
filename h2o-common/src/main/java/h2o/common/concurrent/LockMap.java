package h2o.common.concurrent;

import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;

import java.util.concurrent.locks.Lock;

public class LockMap {

    private final InstanceTable<String, Lock> lockIT = new InstanceTable<String, Lock>(new AbstractInstanceFactory<Lock>() {

        @Override
        public Lock create(Object id) {
            return new java.util.concurrent.locks.ReentrantLock();
        }

    });


    public Lock getLock(String key) {
        return getLock(key, true);
    }

    public Lock getLock(String key, boolean create) {
        return lockIT.get(key, create);

    }

    public void removeLock(String key) {
        lockIT.remove(key);
    }

}
