package h2o.event;

public interface EventModem<E,T> {

    T encode( E event );

    E parse( T data );

}
