package h2o.common.thirdparty.redis;

import h2o.common.lang.Val;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.RedisCodec;

public class RedisClusterClientProvider extends AbstractRedisProvider implements RedisProvider {

    private final RedisClusterClient client;

    public RedisClusterClientProvider(RedisClusterClient client) {
        this.client = client;
    }

    @Override
    public Redis<String, String> create() {
        StatefulRedisClusterConnection<String, String> conn = client.connect();
        return this.proxy( conn , conn.sync() );
    }

    @Override
    public <K, V> Redis<K, V> create( Val<RedisCodec<K, V>> codec ) {
        StatefulRedisClusterConnection<K, V> conn = client.connect( codec.orElse((RedisCodec<K, V>) this.defaultCodec ) );
        return this.proxy( conn , conn.sync() );
    }


}
