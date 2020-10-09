package h2o.event.impl;

import h2o.common.util.collection.MapBuilder;
import h2o.event.Event;
import h2o.event.EventEncoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zhangjianwei on 2017/5/20.
 */
public class TypeEventEncoderProxy implements EventEncoder<String> {

    private static final Logger log = LoggerFactory.getLogger( TypeEventEncoderProxy.class.getName() );


    private final Map<String,EventEncoder<String>> ees = MapBuilder.newConcurrentHashMap();

    @Override
    public Event parse(String strEvent ) {

        String type = StringUtils.substringBefore( strEvent , ":" );

        EventEncoder<String> eventEncoder = ees.get( type );
        if( eventEncoder == null ) {
            log.error("没有对应的解码处理器[{}]",type);
            return null;
        }

        String se = StringUtils.substringAfter( strEvent , ":" );

        return eventEncoder.parse( se );

    }

    @Override
    public String encode( Event event ) {

        if( event == null ) {
            return null;
        }

        String type = event.getEventType();
        EventEncoder<String> eventEncoder = ees.get( type );
        if( eventEncoder == null ) {
            log.error("没有对应的编码处理器[{}]",type);
            return null;
        }

        return type + ":" + eventEncoder.encode( event );

    }


    public TypeEventEncoderProxy regEventEncoder(String eventType, EventEncoder<String> eventEncoder ) {
        ees.put(eventType,eventEncoder);
        return this;
    }


    // Spring
    public void setEventEncoders(Map<String, EventEncoder<String>> ees) {
        if( ees != null && !ees.isEmpty()) {
            for( Map.Entry<String, EventEncoder<String>> ee : ees.entrySet() ) {
                regEventEncoder(ee.getKey(),ee.getValue());
            }
        }
    }




}
