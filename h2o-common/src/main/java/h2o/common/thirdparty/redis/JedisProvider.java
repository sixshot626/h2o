package h2o.common.thirdparty.redis;

import redis.clients.jedis.JedisCommands;

public interface JedisProvider {

    JedisCommands getJedis();

    void release( JedisCommands jedis );

    <T> T callback( JedisCallBack<T> jedisCallBack);

    <T> T callback( JedisCallBack<T> jedisCallBack , boolean isSilently );

}
