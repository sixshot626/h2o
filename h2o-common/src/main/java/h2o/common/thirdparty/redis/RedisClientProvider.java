package h2o.common.thirdparty.redis;

import h2o.common.lang.Val;
import io.lettuce.core.RedisClient;
import io.lettuce.core.codec.RedisCodec;


public class RedisClientProvider extends AbstractRedisProvider implements RedisProvider {

    private final RedisClient client;

    public RedisClientProvider(RedisClient client) {
        this.client = client;
    }


    @Override
    public Redis<String, String> create() {
        return this.proxy(client.connect().sync());
    }

    @Override
    public <K, V> Redis<K, V> create(Val<RedisCodec<K, V>> codec) {
        return this.proxy(client.connect(codec.orElse((RedisCodec<K, V>) this.codec)).sync());
    }


}
