package h2o.common.collection;

import h2o.common.exception.ReadonlyException;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.lang.tuple.TupleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentBiMap<K, V> {

	private final ConcurrentHashMap<K, V> m1 = new ConcurrentHashMap<K, V>();
	private final ConcurrentHashMap<V, K> m2 = new ConcurrentHashMap<V, K>();

	private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
	
	private volatile boolean readonly = false;

	
	public void readonlyMode() {
		
		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			this.readonly = true;
		} finally {
			lock.unlock();
		}
		
	}

	public boolean isEmpty() {
		
		if( this.readonly ) {
			return m1.isEmpty();
		}
		
		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m1.isEmpty();
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		
		if( this.readonly ) {
			return m1.size();
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m1.size();
		} finally {
			lock.unlock();
		}
	}

	public V get(Object key) {
		
		if( this.readonly ) {
			return m1.get(key);
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m1.get(key);
		} finally {
			lock.unlock();
		}
	}
	
	public K getKey(Object value) {
		
		if( this.readonly ) {
			return m2.get(value);
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m2.get(value);
		} finally {
			lock.unlock();
		}
	}

	public boolean containsKey(Object key) {
		
		if( this.readonly ) {
			return m1.containsKey(key);
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m1.containsKey(key);
		} finally {
			lock.unlock();
		}
	}

	public boolean containsValue(Object value) {
		
		if( this.readonly ) {
			return m2.containsKey(value);
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return m2.containsKey(value);
		} finally {
			lock.unlock();
		}
	}

	public void put(K key, V value) {
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			m1.put(key, value);
			m2.put(value, key);
		} finally {
			lock.unlock();
		}
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}

			for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
				m1.put(e.getKey(), e.getValue());
				m2.put(e.getValue(), e.getKey());
			}

		} finally {
			lock.unlock();
		}
	}

	public void remove(Object key) {
		
		if( key == null ) {
			return;
		}
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			Object v = m1.get(key);
			if( v != null ) {
				m1.remove(key);
				m2.remove(v);
			}
		} finally {
			lock.unlock();
		}
	}

	public void removeValue(Object value) {
		
		if( value == null ) {
			return;
		}
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			Object k = m2.get(value);
			if( k != null ) {
				m1.remove(k);
				m2.remove(value);				
			}
		} finally {
			lock.unlock();
		}
	}

	public V replace(K key, V value) {
		
		if( key == null || value == null ) {
			throw new NullPointerException();
		}
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			V oldV = m1.get(key);			
			if( oldV == null ) {
				throw new NullPointerException();
			}
			
			m1.replace(key, value);			
			m2.remove(oldV);
			m2.put(value, key);
			
			return oldV;
			
		} finally {
			lock.unlock();
		}
	}
	
	public K replaceKey(K key, V value) {
		
		if( key == null || value == null ) {
			throw new NullPointerException();
		}
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			K oldK = m2.get(value);			
			if( oldK == null ) {
				throw new NullPointerException();
			}
			
			m2.replace(value, key);			
			m1.remove(oldK);
			m1.put(key, value);
			
			return oldK;
			
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		
		if( this.readonly ) {
			throw new ReadonlyException();
		}

		Lock lock = rwlock.writeLock();
		lock.lock();
		try {
			
			if( this.readonly ) {
				throw new ReadonlyException();
			}
			
			m1.clear();
			m2.clear();
		} finally {
			lock.unlock();
		}
	}
	
	public Map<K, V> getM1() {
		
		Map<K, V> m = new HashMap<K, V>();
		m.putAll(m1);
		
		return m;		
		
	}
	
	public Map<V, K> getM2() {
		
		Map<V, K> m = new HashMap<V, K>();
		m.putAll(m2);
		
		return m;		
		
	}
	
	public Tuple2<Map<K, V>, Map<V, K>> getMM() {
		if( this.readonly ) {
			return TupleUtil.t(getM1(), getM2());
		}

		Lock lock = rwlock.readLock();
		lock.lock();
		try {
			return TupleUtil.t(getM1(), getM2());
		} finally {
			lock.unlock();
		}
	}
	
	

	@Override
	public String toString() {
		return m1ToString();
	}
	
	public String m1ToString() {
		return m1.toString();
	}
	
	public String m2ToString() {
		return m2.toString();
	}
	
	
	

}
