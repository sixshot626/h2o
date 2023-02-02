package h2o.utils.log;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public interface TagLogger {

    boolean log( boolean keyLog , LogLevel level,  String[] tags,  String prompt, Object log );

    default boolean log( LogLevel level,  String[] tags,  String prompt, Object log ) {
       return log(false , level , tags , prompt , log );
    }

    default boolean keyLog( LogLevel level,  String[] tags,  String prompt, Object log ) {
        return log(true , level , tags , prompt , log );
    }


    boolean fmtLog( boolean keyLog , LogLevel level,  String[] tags,  String prompt,  String fmt , Object... args );

    default boolean fmtLog( LogLevel level,  String[] tags,  String prompt,  String fmt , Object... args ) {
        return fmtLog( false , level , tags , prompt , fmt , args );
    }

    default boolean fmtKeyLog( LogLevel level,  String[] tags,  String prompt,  String fmt , Object... args ) {
        return fmtLog( true , level , tags , prompt , fmt , args );
    }

}
