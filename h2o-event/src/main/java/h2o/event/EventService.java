package h2o.event;

import h2o.common.lang.SString;

import java.util.function.BiConsumer;

public interface EventService<E> {

    EventPublisher<E> publisher( SString channel );

    void subcribe( String topical , BiConsumer<EventContext,E> consumer );

}
