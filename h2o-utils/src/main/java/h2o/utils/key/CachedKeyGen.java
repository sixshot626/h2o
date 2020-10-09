package h2o.utils.key;

import h2o.common.concurrent.Locks;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.math.IntArith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class CachedKeyGen {

    private static final Logger log = LoggerFactory.getLogger( CachedKeyGen.class.getName() );

    private final int cache_size;
    private final String seqObj;

    private volatile String cyclicSpace = KeyGen.DEFAULT_CYCLICSPACE;

    private final Lock lock = Locks.newLock();

    public CachedKeyGen(String seqObj) {
        this.seqObj = seqObj;
        this.cache_size = 1000;
    }

    public CachedKeyGen(String seqObj, int cache_size) {
        this.seqObj = seqObj;
        this.cache_size = cache_size;
    }

    public String getKey(int length) {
        return KeyGen.leftPad(getKey(), length);
    }


    private volatile KeyCache keyCache = null;

    public String getKey() {


        KeyCache kCache = keyCache;

        String key = kCache == null ? null : kCache.getKey();

        if (key == null) {

            lock.lock();
            try {

                key = keyCache == null ? null : keyCache.getKey();
                if (key == null) {
                    keyCache = incKey();
                    key = keyCache.getKey();
                }

            } finally {
                lock.unlock();
            }

        }

        return key;


    }


    public KeyCache incKey() {

        CyclicSpaceSetter cyclicSpaceSetter = new DefaultCyclicSpaceSetter(cyclicSpace);

        Tuple2<String, String> kk = null;
        int n = 0;
        while (((kk = KeyGen.incAndUpdateKey(seqObj, cyclicSpaceSetter  , Integer.toString(cache_size))) == null) && (n++ < KeyGen.RETRYTIMES)) {
            try {
                TimeUnit.MILLISECONDS.sleep(20L);
            } catch (InterruptedException localInterruptedException) {
            }
        }

        if (kk == null) {
            throw new RuntimeException("getKey 失败!!!");
        }

        log.info("getKey({})============>{}", seqObj, kk);


        return new KeyCache(kk.e1, this.cache_size);


    }


    public String getCyclicSpace() {
        return KeyGen.DEFAULT_CYCLICSPACE.equals(cyclicSpace) ? null : cyclicSpace;
    }

    public void setCyclicSpace(String cyclicSpace) {
        if (cyclicSpace == null) {
            cyclicSpace = KeyGen.DEFAULT_CYCLICSPACE;
        }
        if (!cyclicSpace.equals(this.cyclicSpace)) {
            lock.lock();
            try {
                if (!cyclicSpace.equals(this.cyclicSpace)) {
                    this.cyclicSpace = cyclicSpace;
                    keyCache = incKey();
                }
            } finally {
                lock.unlock();
            }
        }

    }

    public BigInteger getNumberKey() {
        return new BigInteger(getKey());
    }


    private static class KeyCache {

        private final int cache_size;

        private final AtomicInteger i = new AtomicInteger();

        private final String key;

        public KeyCache(String key, int cache_size) {
            this.key = key;
            this.cache_size = cache_size;
        }

        public String getKey() {
            if (i.get() > cache_size) {
                return null;
            }
            int ki = i.incrementAndGet();
            if (ki > cache_size) {
                return null;
            }
            return IntArith.add(key, Integer.toString(ki));
        }

    }


}