package h2o.common.thirdparty.redis;

import h2o.common.lang.Val;
import io.lettuce.core.codec.RedisCodec;

import java.util.function.Function;

public interface RedisProvider {

    Redis<String, String> create();

    <K, V> Redis<K, V> create( Val<RedisCodec<K, V>> codec );

    <R> Val<R> execute( Function<Redis<String,String> , R > func , boolean silent );

    <R,K,V> Val<R> execute( Val<RedisCodec<K, V>> codec , Function<Redis<K,V> , R > func , boolean silent );

}
