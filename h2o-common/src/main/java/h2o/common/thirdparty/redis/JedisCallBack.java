package h2o.common.thirdparty.redis;

import redis.clients.jedis.JedisCommands;

public interface JedisCallBack<T> {
	
	T doCallBack(JedisCommands jedis) throws Exception;

}
