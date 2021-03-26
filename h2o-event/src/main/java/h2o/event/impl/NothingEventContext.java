package h2o.event.impl;

import h2o.common.lang.NBytes;
import h2o.event.EventContext;


public class NothingEventContext implements EventContext {

    @Override
    public void confirm() {}

    @Override
    public void reject() {}

    @Override
    public void hide( java.time.Duration timeout) {}

    @Override
    public void reply( NBytes r ) {}

}
