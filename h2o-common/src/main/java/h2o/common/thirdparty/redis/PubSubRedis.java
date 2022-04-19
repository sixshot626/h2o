package h2o.common.thirdparty.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

public interface PubSubRedis<K, V> extends Redis<K, V>, RedisPubSubCommands<K, V> {

    void addListener(RedisPubSubListener<K, V> listener);

    void removeListener(RedisPubSubListener<K, V> listener);

}
