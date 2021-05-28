package h2o.common.thirdparty.redis;

import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.Rtn;
import h2o.common.lang.Val;
import io.lettuce.core.codec.RedisCodec;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public interface RedisProvider {

    Redis<String, String> create();

    <K, V> Redis<K, V> create( Val<RedisCodec<K, V>> codec );


    PubSubRedis<String, String> createPubSub();

    <K, V> PubSubRedis<K, V> createPubSub( Val<RedisCodec<K, V>> codec );

    default <R> Rtn<R> execute( Function<Redis<String,String> , R > func ) {
        return this.execute( func , true );
    }

    default <R> Rtn<R> execute( Function<Redis<String,String> , R > func , boolean silent ) {

        try ( Redis<String,String> redis = this.create() ) {

            return Rtn.ok( func.apply( redis ));

        } catch ( Exception e ) {

            LoggerFactory.getLogger(this.getClass().getName()).error("" , e );

            if ( silent ) {
                return Rtn.err( e.getMessage() );
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }

        }
    }

    default <K,V,R> Rtn<R> execute( Val<RedisCodec<K, V>> codec , Function<Redis<K,V> , R > func ) {
        return this.execute( codec , func , true );
    }

    default <K,V,R> Rtn<R> execute(Val<RedisCodec<K, V>> codec , Function<Redis<K,V> , R > func , boolean silent ) {

        try ( Redis<K,V> redis = this.create( codec ) ) {

            return Rtn.ok( func.apply( redis ));

        } catch ( Exception e ) {

            LoggerFactory.getLogger(this.getClass().getName()).error("" , e );

            if ( silent ) {
                return Rtn.err( e.getMessage() );
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }

        }
    }



}
