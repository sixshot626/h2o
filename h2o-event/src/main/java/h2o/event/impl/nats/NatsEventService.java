package h2o.event.impl.nats;


import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.ByteArray;
import h2o.common.lang.Val;
import h2o.common.result.TransReturn;
import h2o.common.result.TransStatus;
import h2o.common.result.TriState;
import h2o.event.EventContext;
import h2o.event.EventModem;
import h2o.event.EventSender;
import h2o.event.EventService;
import h2o.event.impl.NothingEventContext;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;



public class NatsEventService<E> implements EventService<E> {

    private final Options options;

    private final EventModem<E,ByteArray> modem;

    private Val<Connection> connectionVal = Val.empty();

    public NatsEventService(Options options , EventModem<E,ByteArray> modem ) {
        this.options = options;
        this.modem = modem;
    }

    public void init() {
        try {
            connectionVal = new Val<>(Nats.connect(options));
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    public void close() {
        if( this.connectionVal.isPresent() ) {
            try {
                this.connectionVal.get().close();
                this.connectionVal = Val.empty();
            } catch (Exception e) {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }
    }

    protected TransStatus<TriState> realPost( String subject, ByteArray body ) {

        try {
            this.connectionVal.get().publish( subject , body.get() );
            return new TransReturn<TriState, Object>().setStatus( TriState.SUCCESS );
        } catch ( Exception e ) {
            return new TransReturn<TriState, Object>().setStatus( TriState.FAILURE );
        }

    }

    protected void realSubcribe( String topical, Consumer<ByteArray> consumer) {

        this.connectionVal.get().createDispatcher(msq-> consumer.accept( new ByteArray(msq.getData()) ) )
                .subscribe( topical );

    }



    private final EventSender<E> eventSender = new EventSender<E>() {

        @Override
        public void close() throws IOException {
        }

        @Override
        public TransStatus<TriState> post(String subject, E event) {
            return NatsEventService.this.realPost( subject , NatsEventService.this.modem.encode( event ) );
        }
    };


    @Override
    public EventSender<E> sender(String channel) {
        return eventSender;
    }

    @Override
    public void subcribe(String topical, BiConsumer<EventContext, E> consumer ) {
        this.realSubcribe( topical , data-> consumer.accept(new NothingEventContext() , this.modem.parse(data) ) );
    }



}
