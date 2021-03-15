package h2o.common.cluster;

import h2o.common.Mode;
import h2o.common.lang.EBoolean;
import h2o.common.lang.SString;
import h2o.common.thirdparty.redis.Redis;
import h2o.common.thirdparty.redis.RedisProvider;
import h2o.common.util.id.RandomString;
import h2o.common.util.id.UuidUtil;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class ClusterLock {

    private static final Logger log = LoggerFactory.getLogger( ClusterLock.class.getName() );

    private static final long DEFAULT_TIME_OUT = 5000;

    private final String id;

    private final String key;

    private final long expire;  // 秒


    private final RedisProvider redisClient;


    public ClusterLock(RedisProvider redisClient, SString id, String key, Duration expire ) {
        this.id = id.orElse( uuid() );
        this.redisClient = redisClient;
        this.key = "H2OClusterLock_" + key;
        this.expire = expire.get( ChronoUnit.SECONDS );
    }


    public EBoolean isLocked() {

        try ( Redis<String, String> redis = this.redisClient.create() ) {

            return EBoolean.valueOf( id.equals( redis.get(key) ));

        } catch ( Exception e ) {
            log.error( "" , e);
        }

        return EBoolean.NULL;
    }


    private boolean tryLock( Redis<String, String> redis ) {

        if ( "OK".equals( redis.set(key , id , new SetArgs().nx().ex( expire ) ) ) ) {

            return true;

        } else if ( id.equals( redis.get(key) ) )  {

            redis.expire(key, expire);
            return true;

        } else {

            return false;

        }

    }



    public EBoolean tryLock() {

        try ( Redis<String, String> redis = this.redisClient.create() ) {

            return EBoolean.valueOf(tryLock( redis ));

        } catch (Exception e) {

            e.printStackTrace();
            log.error( "" , e);

            return EBoolean.NULL;

        }

    }


    public EBoolean lock() {
        return lock( DEFAULT_TIME_OUT );
    }

    public EBoolean lock( long timeout ) {

        EBoolean lock = EBoolean.NULL;

        try ( Redis<String, String> redis = this.redisClient.create() ) {


            long t = System.currentTimeMillis();

            do {

                try {
                    if (tryLock(redis)) {
                        lock = EBoolean.TRUE;
                        return EBoolean.TRUE;
                    } else {
                        lock = EBoolean.FALSE;
                    }
                } catch ( Exception e ) {
                    log.error("" , e );
                    lock = EBoolean.NULL;
                }


                try {

                    TimeUnit.MILLISECONDS.sleep(50);

                } catch (InterruptedException e) {
                }

            } while (System.currentTimeMillis() - t < timeout);

        } catch ( Exception e ) {
            log.error("" , e);
        }

        return lock;

    }



    private void unlockUNLUA( Redis<String,String> redis ) {

        if ( id.equals( redis.get(key) ) ) {
            redis.del(key);
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


    public void unlock() {

        try ( Redis<String, String> redis = this.redisClient.create() ) {

            if (UNSUPPORT_LUA) {

                unlockUNLUA(redis);

            } else {

                try {

                    Object result = redis.eval( UNLOCK_LUA , ScriptOutputType.INTEGER , new String[] { key } , new String[] { id }  );
                    if ( result == null ) {
                        unlockUNLUA(redis);
                    }


                } catch (Throwable e) {

                    log.error("", e);

                    unlockUNLUA(redis);

                }
            }

        } catch ( Exception e ) {
            log.error( "" , e);
        }

    }


    private static synchronized String uuid() {
        return UuidUtil.getUuid() + "-" + new RandomString().makeNumberCode(10);
    }

}
