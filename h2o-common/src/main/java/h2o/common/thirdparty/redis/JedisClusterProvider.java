package h2o.common.thirdparty.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

public class JedisClusterProvider extends AbstractJedisProvider implements JedisProvider{

    private static final Logger log = LoggerFactory.getLogger( JedisClusterProvider.class.getName() );


    private final JedisCluster jedisCluster;

    public JedisClusterProvider(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public JedisCluster getJedis() {

        try {

            return jedisCluster;

        } catch ( Exception e ) {
            log.error( "" , e );
            return null;
        }

    }

    @Override
    public void release( JedisCommands jedis ) {}
}
