package h2o.common.thirdparty.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

public class JedisPoolProvider extends AbstractJedisProvider implements JedisProvider{

    private static final Logger log = LoggerFactory.getLogger( JedisPoolProvider.class.getName() );


    private final JedisPool jedisPool;

    public JedisPoolProvider(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Jedis getJedis() {

        try {

            return jedisPool.getResource();

        } catch ( Exception e ) {
            log.error( "" , e );
            return null;
        }

    }

    @Override
    public void release( JedisCommands jedis ) {
        JedisUtil.close( (Jedis) jedis);
    }
}
