package h2o.common.thirdparty.redis;

import io.lettuce.core.api.sync.*;

public interface Redis<K,V> extends AutoCloseable ,
        BaseRedisCommands<K, V>, RedisGeoCommands<K, V>, RedisHashCommands<K, V>, RedisHLLCommands<K, V>,
        RedisKeyCommands<K, V>, RedisListCommands<K, V>, RedisScriptingCommands<K, V>, RedisServerCommands<K, V>,
        RedisSetCommands<K, V>, RedisSortedSetCommands<K, V>, RedisStreamCommands<K, V>, RedisStringCommands<K, V> {
}
