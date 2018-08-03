package h2o.common.concurrent.factory;

import h2o.common.concurrent.RunUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InstancePool<I> {

    private final InstanceFactory<I> factory;


    private final ArrayBlockingQueue<I> cache;
    private final ConcurrentLinkedQueue<I> queue = new ConcurrentLinkedQueue<I>();

    private volatile boolean stop = false;

    public InstancePool(InstanceFactory<I> factory, int n) {
        this.factory = factory;
        this.cache = new ArrayBlockingQueue<I>(n);
    }

    public I get() {

        if ( stop ) {
            return null;
        }

        I ins = cache.poll();
        if ( ins == null ) {
            ins = queue.poll();
        }

        if ( ins == null ) {
            ins = factory.create(null);
        }

        return ins;

    }

    public void release( I ins ) {

        if ( ins == null ) {
            return;
        }

        if ( stop ) {

            this.free(ins);

        } else {

            if ( !cache.offer( ins ) ) {
                queue.offer(ins);
            }

            if ( stop ) {
               this.close();
            }
        }

    }

    public int clear( int n ) {

        if ( queue.isEmpty() ) {
            return 0;
        }

        int i = 0;
        while ( n < 0 || i++ < n ) {
            if ( queue.isEmpty() ) {
                return 0;
            }
            free( queue.poll() );
        }

        return 1;

    }

    public void close() {

        this.stop = true;

        this.clear( -1 );

        while ( !cache.isEmpty()  ) {
            free( cache.poll() );
        }

    }


    private void free( I ins ) {
        if ( ins != null ) {
            factory.free(null, ins);
        }
    }


}
