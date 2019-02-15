package h2o.common.cluster;

import h2o.common.thirdparty.redis.JedisCallBack;
import h2o.common.thirdparty.redis.JedisProvider;
import h2o.common.util.id.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class ClusterLock {

    private static final Logger log = LoggerFactory.getLogger( ClusterLock.class.getName() );

    private static final long DEFAULT_TIME_OUT = 30000;

    private final String id = UuidUtil.getUuid();

    private final JedisProvider jedisProvider;

    private final Jedis _jedis;

    private final String key;

    private final int expire;

    volatile boolean run = true;

    private volatile boolean locked = false;


    public ClusterLock(JedisProvider jedisProvider, String key, int expire ) {
        this.jedisProvider = jedisProvider;
        this._jedis = null;
        this.key = "H2OClusterLock_" + key;
        this.expire = expire;
    }

    public ClusterLock( Jedis jedis, String key, int expire ) {
        this.jedisProvider = null;
        this._jedis = jedis;
        this.key = "H2OClusterLock_" + key;
        this.expire = expire;
    }


    private void tryLock( Jedis jedis ) {

        if ( "OK".equals(jedis.set( key, id ,  "NX" , "EX" , expire ) ) ) {

            locked = true;

        } else if ( id.equals(jedis.get(key) ) )  {

            jedis.expire(key, expire);
            locked = true;

        } else {

            locked = false;

        }

    }

    public boolean tryLock() {

        try {

            if ( jedisProvider == null ) {

                tryLock( this._jedis );

            } else jedisProvider.callback(new JedisCallBack<Void>() {

                @Override
                public Void doCallBack(Jedis jedis) throws Exception {

                    tryLock( jedis );

                    return null;

                }

            });

        } catch (Exception e) {

            e.printStackTrace();
            log.error( "" , e);

            locked = false;

        }

        return locked;

    }


    public boolean lock() {
        return lock( DEFAULT_TIME_OUT );
    }

    public boolean lock( long timeout ) {

        long t = System.currentTimeMillis();

        do {

            if ( tryLock() ) {
                return true;
            }

            try {

                TimeUnit.MILLISECONDS.sleep(50 );

            } catch (InterruptedException e) {
            }

        } while ( System.currentTimeMillis() - t < timeout);

        return false;

    }



    private void unlock( Jedis jedis ) {

        if ( id.equals( jedis.get(key) ) ) {
            jedis.del(key);
        }

    }

    public void unlock() {

        locked = false;

        try {

            if ( jedisProvider == null ) {

                unlock( this._jedis );

            } else jedisProvider.callback(new JedisCallBack<Void>() {

                @Override
                public Void doCallBack(Jedis jedis) throws Exception {

                    unlock( jedis );

                    return null;

                }

            });

        } catch ( Exception e ) {

            e.printStackTrace();
            log.error( "" , e);

        }

    }

    public boolean isLocked() {
        return run && locked;
    }

}
