package h2o.event;

import h2o.common.lang.NBytes;

import java.time.Duration;

public interface EventContext {

    void confirm();

    void reject();

    void hide( Duration timeout );

    void reply( NBytes r );

}
