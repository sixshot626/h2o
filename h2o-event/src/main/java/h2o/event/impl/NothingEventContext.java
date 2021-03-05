package h2o.event.impl;

import h2o.event.EventContext;

/**
 * Created by zhangjianwei on 2017/5/23.
 */
public class NothingEventContext implements EventContext {

    @Override
    public void confirm() {}

    @Override
    public void reject() {}

    @Override
    public void delete() {}

    @Override
    public void reply( Object r ) {}

}
