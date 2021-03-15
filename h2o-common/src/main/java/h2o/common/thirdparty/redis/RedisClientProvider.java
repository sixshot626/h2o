package h2o.common.thirdparty.redis;

import h2o.common.lang.Val;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;


public class RedisClientProvider extends AbstractRedisProvider implements RedisProvider {

    private final RedisClient client;

    public RedisClientProvider(RedisClient client) {
        this.client = client;
    }


    @Override
    public Redis<String, String> create() {
        StatefulRedisConnection<String, String> conn = client.connect();
        return this.proxy( conn , conn.sync() );
    }

    @Override
    public <K, V> Redis<K, V> create( Val<RedisCodec<K, V>> codec ) {
        StatefulRedisConnection<K, V> conn = client.connect(codec.orElse((RedisCodec<K, V>) this.defaultCodec));
        return this.proxy( conn , conn.sync() );
    }


    public void shutdown() {
        client.shutdown();
    }
}
