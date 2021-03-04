package h2o.event;

import java.util.function.BiConsumer;

public interface EventService<E> {

    EventSender<E> sender( String channel );

    void subcribe( String topical , BiConsumer<EventContext,E> consumer );

}
