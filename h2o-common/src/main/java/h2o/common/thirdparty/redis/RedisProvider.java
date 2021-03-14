package h2o.common.thirdparty.redis;

import h2o.common.lang.Val;
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;

import java.util.concurrent.CompletableFuture;

public interface RedisProvider {

    Redis<String, String> create();

    <K, V> Redis<K, V> create( Val<RedisCodec<K, V>> codec);

}
