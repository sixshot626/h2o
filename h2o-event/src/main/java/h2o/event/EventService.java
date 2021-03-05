package h2o.event;

import h2o.common.lang.SString;

import java.util.function.BiConsumer;

public interface EventService<E> {

    default EventPublisher<E> publisher() {
        return this.publisher( new SString() );
    }

    EventPublisher<E> publisher( SString channel );

    void subcribe( String topical , SString group , BiConsumer<EventContext,E> consumer );

}
