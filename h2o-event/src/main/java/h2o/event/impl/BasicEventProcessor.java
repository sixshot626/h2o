package h2o.event.impl;

import h2o.common.util.collection.MapBuilder;
import h2o.event.Event;
import h2o.event.EventHandler;
import h2o.event.EventProcessor;

import java.util.Map;

/**
 * Created by zhangjianwei on 2017/5/20.
 */
public class BasicEventProcessor extends AbstractEventProcessor implements EventProcessor {

    private final Map<String,EventHandler> ehs = MapBuilder.newConcurrentHashMap();


    @Override
    protected EventHandler getEventHandler(Event event) {
        return ehs.get( event.getEventType() );
    }


    public void regEventHandler(String eventType, EventHandler eventHandler) {
        ehs.put(eventType,eventHandler);
    }

    // Spring
    public void setEventHandlers(Map<String, EventHandler> ehs) {
        this.ehs.putAll(ehs);
    }


}
