package h2o.event.impl.nats;


import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.ByteArray;
import h2o.common.lang.SString;
import h2o.common.lang.Val;
import h2o.event.EventContext;
import h2o.event.EventPublisher;
import h2o.event.EventService;
import io.nats.client.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public class NatsEventService<E> implements EventService {


    protected final Options options;
    protected volatile Val<Connection> connectionVal = Val.empty();

    public NatsEventService( Options options ) {
        this.options = options;
    }


    @Override
    public void init() {
        try {
            this.connectionVal = new Val<>( Nats.connect( options ) );
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public void stop() {
        if( this.connectionVal.isPresent() ) {
            try {
                this.connectionVal.get().close();
                this.connectionVal = Val.empty();
            } catch (Exception e) {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }
    }


    @Override
    public void subcribe( String topical, SString group ,  BiConsumer<EventContext , ByteArray> consumer ) {

        Dispatcher dispatcher = this.connectionVal.get()
                .createDispatcher(msg -> consumer.accept( new NatsEventContext(msg) ,  new ByteArray(msg.getData())));

        if ( group.isPresent() ) {
            dispatcher.subscribe( topical , group.get() );
        } else {
            dispatcher.subscribe( topical );
        }

    }


    protected void natsPublish( String subject, ByteArray body ) {
        this.connectionVal.get().publish( subject , body.get() );
    }

    protected CompletableFuture<ByteArray> natsRequest( String subject, ByteArray body ) {

        CompletableFuture<Message> res = this.connectionVal.get().request(subject, body.get());

        return res.thenApply( msg-> new ByteArray(msg.getData()) );

    }


    protected final EventPublisher eventPublisher = new EventPublisher() {

        @Override
        public void close() throws IOException {
        }

        @Override
        public void publish( String subject, ByteArray event ) {
            natsPublish( subject , event );
        }

        @Override
        public CompletableFuture<ByteArray> request( String subject, ByteArray event ) {
            return natsRequest( subject , event );
        }

    };


    @Override
    public EventPublisher publisher( SString channel ) {
        return eventPublisher;
    }

}
