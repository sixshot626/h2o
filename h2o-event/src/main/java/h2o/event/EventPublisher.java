package h2o.event;

import h2o.common.result.TransStatus;
import h2o.common.result.TriState;

import java.io.Closeable;

public interface EventPublisher<E> extends Closeable {

    TransStatus<TriState> publish( String subject , E event );

}
