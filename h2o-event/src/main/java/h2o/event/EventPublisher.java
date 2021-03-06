package h2o.event;

import h2o.common.lang.ByteArray;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface EventPublisher extends Closeable {

    void publish( String subject , ByteArray event );

    CompletableFuture<ByteArray> request(String subject , ByteArray event );

}
