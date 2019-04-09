package h2o.event.impl.redis;

import h2o.common.collections.builder.ListBuilder;
import h2o.common.thirdparty.redis.JedisCallBack;
import h2o.event.Event;
import h2o.event.EventReceiver;
import h2o.event.impl.AbstractDispatcherEventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;

import java.util.List;

/**
 * Created by zhangjianwei on 16/7/2.
 */
public class RedisEventReceiver extends AbstractDispatcherEventReceiver implements EventReceiver {


    private static final Logger log = LoggerFactory.getLogger( RedisEventReceiver.class.getName() );

    private final RedisEventHelper helper;

    public RedisEventReceiver( RedisEventHelper redisHelper ) {
        super( 30000 , 10 , 300000 );
        this.helper = redisHelper;
    }


    @Override
    protected List<Event> recvEvent() {

        final List<Event> events = ListBuilder.newList();

        helper.jedisProvider.callback(new JedisCallBack<Void>() {

            @Override
            public Void doCallBack(JedisCommands jedis) throws Exception {

                for(int i = 0 ; i < 5 ; i++ ) {
                    String strEvent = jedis.rpop(helper.eventQueueName);
                    if( strEvent == null ) {
                        break;
                    }
                    Event event = helper.parse(strEvent);
                    if( event != null ) {
                        events.add( event );
                    }
                }

                return null;
            }

        });

        log.debug("recvEvent ========= {} " , events );

        return events;
    }




}
