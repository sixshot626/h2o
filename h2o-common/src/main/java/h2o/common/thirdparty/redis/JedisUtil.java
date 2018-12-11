package h2o.common.thirdparty.redis;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class JedisUtil extends AbstractJedisProvider implements JedisProvider {

    private static final Logger log = LoggerFactory.getLogger( JedisUtil.class.getName() );

    private final RedisConfig[] redisConfigs;

	private volatile int ji = -1;

	public JedisUtil(String... confs ) {

		redisConfigs = new RedisConfig[confs.length];

		int i = 0;
		for( String conf : confs ) {

		    //host:port_1@abcd

		    String pass = null;
		    if( StringUtils.contains( conf , '@' ) ) {
                pass = StringUtils.substringAfter( conf , "@");
                conf = StringUtils.substringBefore( conf , "@");
            }

            Integer db = null;
            if( StringUtils.contains( conf , '_' ) ) {
                db = Integer.parseInt(StringUtils.substringAfter( conf , "_"));
                conf = StringUtils.substringBefore( conf , "_");
            }

			String host = StringUtils.substringBefore( conf , ":");
			String p = StringUtils.substringAfter( conf , ":");
			Integer port = StringUtils.isBlank(p) ? 6379 : new Integer(p);

			redisConfigs[i++] = new RedisConfig( host , port , pass  ,  db );
		}

	}


    public JedisUtil(RedisConfig... confs ) {
        redisConfigs = confs;
    }


	public Jedis getJedis() {

		int i = ji;
		Jedis j = getJedis(i);

		if( check(j) ) {
			return j;
		} else {
			return select();
		}

	}

    private Jedis select() {

        for (int i = 0; i < redisConfigs.length; i++) {
            Jedis j = getJedis(i);
            if (check(j)) {
                ji = i;
                return j;
            }
        }

        ji = -1;

        return null;

    }



    private Jedis getJedis( int i ) {

		if ( i == -1 ) {
		    return null;
        }

        try {

            return getJedis(redisConfigs[i]);

        } catch ( Exception e ) {

		    log.error( "" ,  e );

		    return null;

        }

	}


	protected Jedis getJedis( RedisConfig conf ) {

        Jedis jedis = conf.timeout == null ?
                new Jedis(conf.host, conf.port) :
                new Jedis(conf.host, conf.port , conf.timeout);


        if ( conf.pass != null ) {
            jedis.auth(conf.pass);
        }

        if ( conf.db != null ) {
            jedis.select( conf.db );
        }

        return jedis;
    }



	protected boolean check( Jedis jedis ) {

		if (jedis == null) {
			return false;
		}

		try {
			return "PONG".equals(jedis.ping());
		} catch (Exception e) {
			log.debug("Check Jedis[{}] error!", jedis);
            release(jedis);
		}

		return false;

	}



    public void release( Jedis jedis ) {
		close( jedis );
	}


	public static void close( Jedis jedis ) {
        if( jedis != null ) try {
            jedis.close();
        } catch (Exception e) {
            log.error("Close Jedis[" + jedis + "] error!", e );
        }
    }




}
