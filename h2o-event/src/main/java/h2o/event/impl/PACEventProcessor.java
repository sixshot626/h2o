package h2o.event.impl;


import h2o.common.concurrent.pac.Consumer;
import h2o.common.concurrent.pac.ConsumersController;
import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.lang.tuple.TupleUtil;
import h2o.event.Event;
import h2o.event.EventContext;
import h2o.event.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

/**
 * Created by zhangjianwei on 16/7/2.
 */
public class PACEventProcessor extends BasicEventProcessor implements EventProcessor {

    private static final Logger log = LoggerFactory.getLogger( PACEventProcessor.class.getName() );


    private final ConsumersController<Tuple2<EventContext,Event>> cc;

    public PACEventProcessor(int n ) {
    	
        cc = new ConsumersController<Tuple2<EventContext,Event>>(Executors.newFixedThreadPool(n),new ArrayBlockingQueue<Tuple2<EventContext,Event>>(n));
        cc.addConsumers( new Consumer<Tuple2<EventContext,Event>>() {

            @Override
            public void consume(Tuple2<EventContext,Event> t ) {
                _procEvent( t.e0 , t.e1 );
            }

        } , n );

    }


    private void _procEvent( EventContext context , Event event ) {
        super.proc( context , event );
    }


    @Override
    public void proc( EventContext context , Event event) {

        try {

            cc.putProduct( TupleUtil.t( context , event ) );

        } catch ( Exception e ) {

            log.error("",e);
            throw ExceptionUtil.toRuntimeException(e);

        }


    }


    public void done() {
        cc.done();
    }


}
