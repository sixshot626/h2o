package h2o.event;

import h2o.common.lang.NBytes;
import h2o.common.lang.NString;

import java.util.function.BiConsumer;

public interface EventService {

    default EventPublisher publisher() {
        return this.publisher( new NString() );
    }

    EventPublisher publisher( NString channel );

    void subcribe(String topical , NString group , BiConsumer<EventContext, NBytes> consumer );

    void init();

    void stop();

}
