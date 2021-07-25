package h2o.common.util.lang;

import h2o.common.util.collection.ListBuilder;

import java.util.List;

public abstract class ArgsUtil {

    private ArgsUtil() {}

    public static boolean isBlank( Object[] args ) {
        return args == null || args.length == 0 || ( args.length == 1 && args[0] == null);
    }

    public static <T> T[] more(T arg , T... more ) {
       return more2List(arg , more).toArray((T[])new Object[0] );
    }


    public static <T> List<T> more2List(T arg , T... more ) {

        if ( more == null || more.length == 0 ) {
            List<T> argList = ListBuilder.newList(1);
            argList.add(arg);
            return argList;
        } else {
            List<T> argList = ListBuilder.newList(more.length + 1 );
            argList.add(arg);
            for ( T a : more ) {
                argList.add(a);
            }
            return argList;
        }
    }


    public static <T> List<T> toList(T... args ) {
        if ( args == null || args.length == 0 ) {
            return ListBuilder.newList(0);
        } else {
            List<T> argList = ListBuilder.newList(args.length);
            for ( T a : args ) {
                argList.add(a);
            }
            return argList;
        }
    }

    public static  <T> T[] rest(T... args ) {
        if ( args == null || args.length < 2 ) {
            return (T[]) new Object[0];
        } else {
            T[] tailArgs = (T[]) new Object[ args.length - 1 ];
            System.arraycopy( args , 1 , tailArgs , 0 , tailArgs.length );
            return tailArgs;
        }
    }

}
