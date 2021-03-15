package h2o.common.thirdparty.redis;

import io.lettuce.core.codec.RedisCodec;

import java.lang.reflect.Proxy;

public abstract class AbstractRedisProvider {

    protected RedisCodec<?,?> defaultCodec;

    public void setCodec(RedisCodec<?, ?> codec) {
        this.defaultCodec = codec;
    }


    protected <K, V> Redis<K, V> proxy( final Object conn , final Object redis ) {
        return (Redis<K, V>) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                new Class<?>[]{Redis.class}, ( proxy , method , args ) ->
                        "close".equals(method.getName()) ?
                                method.invoke( conn , args ) : method.invoke(redis, args) );
    }

}
