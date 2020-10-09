package h2o.common.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapBuilder<K,V> {
	
	private Map<K,V> m;
	
	public MapBuilder() {
		this.m = new HashMap<K,V>();
	}
	
	public MapBuilder(int i) {
		this.m = new HashMap<K,V>(i);
	}
	
	public MapBuilder(Map<K,V> m) {
		this.m = m;
	}
	
	public static MapBuilder<String,Object> so() {
		return new MapBuilder<String,Object>();
	}
	
	public static MapBuilder<String,Object> so( int i) {
		return new MapBuilder<String,Object>(i);
	}
	
	public static <X,Y> MapBuilder<X,Y> start() {
		return new MapBuilder<X,Y>();
	}
	
	public static <X,Y> MapBuilder<X,Y> start( int i ) {
		return new MapBuilder<X,Y>(i);
	}
	
	public static <X,Y> MapBuilder<X,Y> start( Map<X,Y> m ) {
		return new MapBuilder<X,Y>(m);
	}
	
	
	public static <X,Y> Map<X,Y> newEmptyMap() {
		return new HashMap<X,Y>(0);
	}
	
	public static <X,Y> Map<X,Y> newMap() {
		return new HashMap<X,Y>();
	}

	public static <X,Y> Map<X,Y> newMap( int i ) {
		return new HashMap<X,Y>(i);
	}
	
	public static <X,Y> Map<X,Y> newMap( Object... kvs ) {
		return new MapBuilder<X,Y>( CollectionUtil.argsIsBlank(kvs) ? 0 : (kvs.length / 2) ).puts(kvs).get();
	}
	
	public static <X,Y> Map<X,Y> newMapAndPutAll( Map<? extends X, ? extends Y> m ) {
		return new MapBuilder<X,Y>( m == null ? 0 : m.size() ).putAll(m).get();
	}
	
	public static <X,Y> ConcurrentHashMap<X,Y> newConcurrentHashMap() {
		return new ConcurrentHashMap<X,Y>();
	}

	public static <X,Y> ConcurrentHashMap<X,Y> newConcurrentHashMap( int i ) {
		return new ConcurrentHashMap<X,Y>(i);
	}
	
	public static <X,Y> ConcurrentHashMap<X,Y> newConcurrentHashMap( Object... kvs ) {
		return  (ConcurrentHashMap<X, Y>) new MapBuilder<X,Y>(
				new ConcurrentHashMap<X,Y>( CollectionUtil.argsIsBlank(kvs) ? 0 : (kvs.length / 2) )
		).puts(kvs).get();
	}

	public static <X,Y> Map<X,Y> newConcurrentHashMapAndPutAll( Map<? extends X, ? extends Y> m ) {
		return new MapBuilder<X,Y>(new ConcurrentHashMap<X,Y>( m == null ? 0 : m.size() )).putAll(m).get();
	}
	


	@SuppressWarnings("unchecked")
	public <X,Y> Map<X,Y> cget() {
		return (Map<X, Y>) this.m;
	}
	
	public  Map<K,V> get() {
		return  this.m;
	}

	public MapBuilder<K,V> clear() {
		m.clear();
		return this;
	}

	public boolean containsKey(Object key) {
		return m.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return m.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		return m.entrySet();
	}

	public boolean isEmpty() {
		return m.isEmpty();
	}

	public Set<K> keySet() {
		return m.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public MapBuilder<K,V> puts( Object... kvs ) {
		
		if( CollectionUtil.argsIsBlank(kvs) ) {
			return this;
		}  
		
		if( kvs.length < 2 || kvs.length % 2 != 0 ) {
			throw new IllegalArgumentException();
		}
		for( int i = 0 ; i < kvs.length ; i+=2 ) {
			m.put((K)kvs[i], (V)kvs[i+1]);
		}
		return this;
	}

	public MapBuilder<K,V> put(K key, V value) {
		m.put(key, value);
		return this;
	}

	public MapBuilder<K,V> putAll(Map<? extends K, ? extends V> m) {
		if( m != null ) this.m.putAll(m);
		return this;
	}

	public MapBuilder<K,V> remove(Object key) {
		m.remove(key);
		return this;
	}

	public int size() {
		return m.size();
	}

	public Collection<V> values() {
		return m.values();
	}


}
