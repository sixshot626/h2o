package h2o.common.collection;


import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ListValueMap<K,E> implements Map<K, List<E>> , java.io.Serializable {
	

	private static final long serialVersionUID = -7789266051851238934L;
	
	private final Map<K, List<E>> lvm;
	
	public ListValueMap() {
		lvm = MapBuilder.newMap();
	}
	
	public ListValueMap( Map<K, List<E>> lvm ) {
		this.lvm = lvm;
	}
	
	protected List<E> createList() {
		return ListBuilder.newList();
	}

	public int size() {
		return lvm.size();
	}

	public boolean isEmpty() {
		return lvm.isEmpty();
	}

	public boolean containsKey(Object key) {
		return lvm.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return lvm.containsValue(value);
	}

	public List<E> get(Object key) {
		return lvm.get(key);
	}
	
	public List<E> getNull2Empty(Object key) {
		List<E> l = lvm.get(key);
		return l == null ? createList() : l;
	}
	
	public E getFirst(Object key) {
		List<E> l = lvm.get(key);
		return ( l == null || l.isEmpty() ) ? null : l.get(0);
	}

	public List<E> put(K key, List<E> value) {
		return lvm.put(key, value);
	}
	
	public void put(K key, E e , int i) {
		List<E> l = lvm.get(key);
		if( l == null ) {
			l = createList();
			put(key,l);
		}
		if( i < 0 ) {
			l.add(e);
		} else {
			l.add(i, e);
		}
	}
	
	public void putElement(K key, E e ) {
		this.put(key, e, -1);
	}

	public List<E> remove(Object key) {
		return lvm.remove(key);
	}

	public void putAll(Map<? extends K, ? extends List<E>> t) {
		lvm.putAll(t);
	}

	public void clear() {
		lvm.clear();
	}

	public Set<K> keySet() {
		return lvm.keySet();
	}

	public Collection<List<E>> values() {
		return lvm.values();
	}

	public Set<Entry<K, List<E>>> entrySet() {
		return lvm.entrySet();
	}

	public Map<K, List<E>> getRealMap() {
		return lvm;
	}

	public boolean equals(Object o) {
		return lvm.equals(o);
	}

	public int hashCode() {
		return lvm.hashCode();
	}

	@Override
	public String toString() {
		return lvm.toString();
	}

}
