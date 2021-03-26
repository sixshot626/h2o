package h2o.event;

import h2o.common.lang.NBytes;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface EventPublisher extends Closeable {

    void publish( String subject , NBytes event );

    CompletableFuture<NBytes> request(String subject , NBytes event );

}
