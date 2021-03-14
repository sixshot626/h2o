package h2o.common.thirdparty.redis;

import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.Val;
import io.lettuce.core.codec.RedisCodec;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.function.Function;

public abstract class AbstractRedisProvider implements RedisProvider {

    protected RedisCodec<?,?> codec;

    public void setCodec(RedisCodec<?, ?> codec) {
        this.codec = codec;
    }


    protected <K, V> Redis<K, V> proxy(final Object redis ) {
        return (Redis<K, V>) Proxy.newProxyInstance(redis.getClass().getClassLoader(),
                new Class<?>[]{Redis.class}, ( proxy , method , args ) -> method.invoke(redis, args));
    }

    @Override
    public <R> Val<R> execute( Function<Redis<String,String> , R > func , boolean silent ) {

        try ( Redis<String,String> redis = this.create() ) {

            return new Val<>( func.apply( redis ) );

        } catch ( Exception e ) {

            LoggerFactory.getLogger(this.getClass().getName()).error("" , e );

            if ( silent ) {
                return Val.empty();
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }

        }
    }

    @Override
    public <R,K,V> Val<R> execute( Val<RedisCodec<K, V>> codec , Function<Redis<K,V> , R > func , boolean silent ) {

        try ( Redis<K,V> redis = this.create( codec ) ) {

            return new Val<>( func.apply( redis ) );

        } catch ( Exception e ) {

            LoggerFactory.getLogger(this.getClass().getName()).error("" , e );

            if ( silent ) {
                return Val.empty();
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }

        }

    }



}
