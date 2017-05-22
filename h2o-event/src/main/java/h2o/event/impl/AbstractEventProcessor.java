package h2o.event.impl;

import h2o.common.Tools;
import h2o.event.Event;
import h2o.event.EventHandler;
import h2o.event.EventProcessor;

/**
 * Created by zhangjianwei on 2017/5/22.
 */
public abstract class AbstractEventProcessor implements EventProcessor {

    @Override
    public void onEvent(Event event) {
        EventHandler eventHandler = getEventHandler(event);
        if( eventHandler == null ) {
            Tools.log.error("没有对应的事件处理器[{}]",event);
        } else {
            eventHandler.proc(event);
        }
    }

    protected abstract EventHandler getEventHandler(Event even);

}
