package h2o.event.impl.redis;

import h2o.common.thirdparty.redis.JedisCallBack;
import h2o.event.Event;
import h2o.event.EventSender;
import redis.clients.jedis.JedisCommands;

/**
 * Created by zhangjianwei on 16/7/3.
 */
public class RedisEventSender implements EventSender {

    private final RedisEventHelper helper;

    public RedisEventSender( RedisEventHelper redisHelper ) {
        this.helper = redisHelper;
    }


    @Override
    public void putEvents( final Event... events) {

        helper.jedisProvider.callback(new JedisCallBack<Void>() {

            @Override
            public Void doCallBack( JedisCommands jedis ) throws Exception {

                for( Event event : events ) {
                    String strEvent = helper.encode(event);
                    if( strEvent != null ) {
                        jedis.lpush( helper.eventQueueName , strEvent );
                    }
                }

                return null;
            }

        } );

    }



}
