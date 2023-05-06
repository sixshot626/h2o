package h2o.common.lang;

import h2o.common.collection.KeyMap;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.lang.ArgsUtil;

import java.util.*;
import java.util.function.Function;

public final class Val<T> implements OptionalValue<T>, java.io.Serializable {

    private static final long serialVersionUID = -5406301526511160202L;

    private static final Val<?> EMPTY = new Val<>();

    private final T value;

    private final boolean setted;

    private final SNumber stamp;

    private final Map<String,Object> meta;

    private Val() {
        this.value = null;
        this.setted = false;
        this.stamp = SNumber.NULL;
        this.meta = null;
    }

    public Val(T value) {
        this.value = value;
        this.setted = true;
        this.stamp = SNumber.NULL;
        this.meta = null;
    }

    public Val(T value, SNumber stamp) {
        this.value = value;
        this.setted = true;
        this.stamp = stamp;
        this.meta = null;
    }


    public Val(T value, Object metaKey , Object metaVal , Object... more) {

        this.value = value;
        this.setted = true;
        this.stamp = SNumber.NULL;

        KeyMap<Object> _meta = new KeyMap<>( new LinkedHashMap<>() );
        _meta.assoc( metaKey , metaVal );
        if ( !ArgsUtil.isBlank( more ) ) {
            for ( int i = 0 , len = more.length ; i < len ; i+=2  ) {
                _meta.assoc( more[i].toString() , more[i+1] );
            }
        }

        this.meta = Collections.unmodifiableMap(_meta.originalMap());
    }

    public Val(T value, Map<String , Object> meta) {

        this.value = value;
        this.setted = true;
        this.stamp = SNumber.NULL;

        if ( CollectionUtil.isEmpty(meta) ) {
            this.meta = null;
        } else {

            KeyMap<Object> _meta = new KeyMap<>(new LinkedHashMap<>(meta.size()));
            _meta.putAll(meta);

            this.meta = Collections.unmodifiableMap(_meta.originalMap());
        }
    }


    private Val(T value, boolean setted, SNumber stamp, Map<String, Object> meta) {
        this.value = value;
        this.setted = setted;
        this.stamp = stamp;
        this.meta = meta;
    }

    public Val<T> meta(String metaKey , Object metaVal , Object... more) {

        KeyMap<Object> _meta = new KeyMap<>( this.meta == null ? new LinkedHashMap<>() :
                new LinkedHashMap<>( this.meta ));

        _meta.put( metaKey , metaVal );
        if ( !ArgsUtil.isBlank( more ) ) {
            for ( int i = 0 , len = more.length ; i < len ; i+=2  ) {
                _meta.put( more[i].toString() , more[i+1] );
            }
        }

        return new Val<>(this.value , this.setted , this.stamp , Collections.unmodifiableMap(_meta.originalMap()) );

    }

    public Val<T> meta(Map<String , Object> meta) {

        if (CollectionUtil.isEmpty(meta)) {
            return this;
        }

        KeyMap<Object> _meta = new KeyMap<>( new LinkedHashMap<>() );


        if ( !CollectionUtil.isEmpty(this.meta) ) {
            _meta.putAll(this.meta);
        }
        _meta.putAll( meta );

        return new Val<>(this.value , this.setted , this.stamp , Collections.unmodifiableMap(_meta.originalMap()) );

    }


    public Map<String, Object> meta() {
        return new KeyMap<>(meta == null ? Collections.emptyMap() : meta);
    }









    public <R> Val<R> get(Object key) {

        if (this.isPresent()) {
            if ( value instanceof Map) {
                return of(((Map<?, ?>) value).get(key) );
            } else if ( key instanceof String ) {
                return of( new JoddBeanUtilVOImpl(true, false).get( value , (String) key ));
            }
        }

        return empty();
    }

    public <R> Val<R> idx(Number i) {
        return this.idx( i.intValue() );
    }

    public <R> Val<R> idx(int i) {

        if (this.isPresent()) {
            if ( value instanceof List) {
                return of(((List<?>) value).get(i));
            } else if ( value instanceof Iterator) {
                return iteratorGetI( (Iterator<?>) value , i );
            } else if ( value instanceof Iterable ) {
                return iteratorGetI(((Iterable<?>) value).iterator(), i);
            }
        }

        return empty();
    }

    private static <R> Val<R> iteratorGetI(Iterator<?> iterator , int i ) {
        for ( int j = 0 ; j < i ; j++ ) {
            iterator.next();
        }
        return of(iterator.next() );
    }


    private static <R> Val<R> of( Object value ) {
        @SuppressWarnings("unchecked")
        Val<R> t = (Val<R>) (value == null ? EMPTY : new Val<>(value));
        return t;
    }



    public static <R> Val<R> valueOf( R value ) {
        if ( value == null ) {
            return empty();
        } else {
            return new Val<>( value);
        }
    }


    public static <T> Val<T> empty() {
        @SuppressWarnings("unchecked")
        Val<T> t = (Val<T>) EMPTY;
        return t;
    }



    public<U> Val<U> map(Function<? super T, ? extends U> mapper) {
        if (!isPresent()) {
            return empty();
        } else {
            return new Val<>(mapper.apply(value));
        }
    }


    public<U> Val<U> flatMap(Function<? super T, Val<U>> mapper) {
        if (!isPresent()) {
            return empty();
        } else {
            return mapper.apply(value);
        }
    }




    public boolean isSetted() {
        return setted;
    }

    @Override
    public T getValue() {
        return value;
    }

    public SNumber getStamp() {
        return stamp;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Val<?> val = (Val<?>) o;
        return Objects.equals(value, val.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? "<null>" : value.toString();
    }

}
