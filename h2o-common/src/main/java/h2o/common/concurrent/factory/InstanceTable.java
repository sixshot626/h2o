package h2o.common.concurrent.factory;

import h2o.common.util.collection.MapBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceTable<K,V> {
	
	private volatile boolean allowNull = false;
	
	private final InstanceFactory<V> instanceFactory;
	
	public InstanceTable<K,V> setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
		return this;
	}

	public InstanceTable( InstanceFactory<V> instanceFactory) {
		this.instanceFactory = instanceFactory;
	}
	
	private final ConcurrentHashMap<K,V> m = new ConcurrentHashMap<K, V>();
	

	public V get(K key ) {
		return get( key , false );
	}
	
	public V getAndCreateIfAbsent(K key) {
		return get( key , true );
	}
	

	
	public V get(K key, boolean create) {
		
			V v = m.get(key);

			if ( v == null && create ) {
				
				V newv = instanceFactory.create( key );
				if( newv == null && !allowNull) {
					throw new RuntimeException(" instanceFactory.create( " + key + " ) == null ");
				}
				
				if( newv != null ) {
					v = m.putIfAbsent(key, newv);
					if(v == null) {
						v = newv;
					} else {
                        instanceFactory.destroy( newv );
                    }
				}
				
			} 

			return v;

	}
	
	
	public V put( K key , V value ) {
		return m.put(key, value);
	}
	
	public V putIfAbsent( K key , V value ) {
		return m.putIfAbsent(key, value);
	}


	public void remove( K key ) {		
		V v = m.remove(key);	
		if( v != null ) {
			instanceFactory.free(key, v);
		}
	}

	public Map<K,V> export() {
	    return new MapBuilder<K,V>().putAll(m).get();
    }



}
