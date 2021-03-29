package h2o.event.impl.nats;

import h2o.common.lang.NBytes;
import h2o.event.EventContext;
import io.nats.client.Message;

import java.time.Duration;

public class NatsEventContext implements EventContext {

    private final Message message;

    public NatsEventContext(Message message) {
        this.message = message;
    }

    @Override
    public void confirm() {}

    @Override
    public void reject() {}

    @Override
    public void hide( Duration timeout ) {}

    @Override
    public void reply( NBytes r ) {
        message.getConnection().publish( message.getReplyTo() ,  r.get()  );
    }

    @Override
    public void complete(boolean ok) {}
}
