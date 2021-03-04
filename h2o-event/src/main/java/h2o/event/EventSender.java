package h2o.event;

import h2o.common.result.TransStatus;
import h2o.common.result.TriState;

import java.io.Closeable;

public interface EventSender<E> extends Closeable {

    TransStatus<TriState> post( String subject , E event );

}
