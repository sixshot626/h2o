package h2o.common.thirdparty.redis;

import h2o.common.exception.ExceptionUtil;
import redis.clients.jedis.JedisCommands;

public abstract class AbstractJedisProvider implements JedisProvider {


    @Override
    public <T> T callback(JedisCallBack<T> jedisCallBack) {
        return callback(jedisCallBack,false);
    }

    @Override
    public <T> T callback(JedisCallBack<T> jedisCallBack, boolean isSilently) {

        JedisCommands jedis = getJedis();

        if( jedis == null ) {
            if(isSilently) {
                return null;
            } else {
                throw new JedisProviderException("Not available jedis!");
            }
        }

        try {
            return jedisCallBack.doCallBack(jedis);
        } catch( Exception e ) {
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            release(jedis);
        }
    }

}
