package h2o.common.cluster;

import h2o.common.Mode;
import h2o.common.Tools;
import h2o.common.thirdparty.redis.JedisCallBack;
import h2o.common.thirdparty.redis.JedisProvider;
import h2o.common.util.id.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClusterLock {

    private static final Logger log = LoggerFactory.getLogger( ClusterLock.class.getName() );

    private static final long DEFAULT_TIME_OUT = 30000;

    private final String id = UuidUtil.getUuid();

    private final JedisProvider jedisProvider;

    private final JedisCommands _jedis;

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

    public ClusterLock( JedisCommands jedis, String key, int expire ) {
        this.jedisProvider = null;
        this._jedis = jedis;
        this.key = "H2OClusterLock_" + key;
        this.expire = expire;
    }


    private void tryLock( JedisCommands jedis ) {

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
                public Void doCallBack( JedisCommands jedis) throws Exception {

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



    private void unlockUNLUA( JedisCommands jedis ) {

        if ( id.equals( jedis.get(key) ) ) {
            jedis.del(key);
        }

    }

    private boolean UNSUPPORT_LUA = Mode.isUserMode("REDIS_UNSUPPORT_LUA");

    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    private void unlock( JedisCommands jedis ) {

        if ( UNSUPPORT_LUA ) {

            unlockUNLUA(jedis);

        } else  try {

                List<String> keys = Collections.singletonList( key );
                List<String> values = Collections.singletonList( id );

                if (jedis instanceof JedisClusterScriptingCommands) {

                    (( JedisClusterScriptingCommands) jedis).eval(UNLOCK_LUA, keys, values);

                } else if (jedis instanceof ScriptingCommands) {

                    (( ScriptingCommands ) jedis).eval(UNLOCK_LUA, keys, values);

                }

        } catch (Throwable e) {

            Tools.log.error(e);

            unlockUNLUA(jedis);

        }
    }





    public void unlock() {

        locked = false;

        try {

            if ( jedisProvider == null ) {

                unlock( this._jedis );

            } else jedisProvider.callback(new JedisCallBack<Void>() {

                @Override
                public Void doCallBack( JedisCommands jedis ) throws Exception {

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
