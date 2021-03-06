package h2o.event;

import h2o.common.lang.ByteArray;
import h2o.common.lang.SString;

import java.util.function.BiConsumer;

public interface EventService {

    default EventPublisher publisher() {
        return this.publisher( new SString() );
    }

    EventPublisher publisher( SString channel );

    void subcribe( String topical , SString group , BiConsumer<EventContext, ByteArray> consumer );

    void init();

    void stop();

}
