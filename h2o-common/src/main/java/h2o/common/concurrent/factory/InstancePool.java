package h2o.common.concurrent.factory;

import h2o.common.concurrent.RunUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class InstancePool<I> {

    private final InstanceFactory<I> factory;


    private final ConcurrentLinkedQueue<I> queue = new ConcurrentLinkedQueue<I>();
    private final AtomicInteger queueSize = new AtomicInteger();

    private final int n;

    private volatile boolean stop = false;

    public InstancePool( InstanceFactory<I> factory, int n ) {
        this.factory = factory;
        this.n = n;
    }

    public I get() {

        if ( stop ) {
            return null;
        }

        I ins = poll();

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

            offer(ins);

            if ( stop ) {
               this.close();
            }
        }

    }

    public void clear() {

        if ( queue.isEmpty() ) {
            return;
        }

        int m = queueSize.get() - n;

        if ( m > 0 ) {

            int i = 0;
            while ( i++ < m ) {
                if (queue.isEmpty()) {
                    return;
                }
                free(poll());
            }

        }

    }

    public void close() {

        this.stop = true;

        while ( true ) {
            if (queue.isEmpty()) {
                return;
            }
            free(queue.poll());
        }

    }



    private I poll() {

        I ins = queue.poll();

        if ( ins != null ) {
            queueSize.decrementAndGet();
        }

        return ins;
    }

    private boolean offer( I ins ) {

        boolean ok = queue.offer(ins);

        if ( ok ){
            queueSize.incrementAndGet();
        }

        return ok;
    }

    private void free( I ins ) {
        if ( ins != null ) {
            factory.free(null, ins);
        }
    }


}
