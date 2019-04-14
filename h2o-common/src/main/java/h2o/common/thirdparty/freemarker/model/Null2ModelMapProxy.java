package h2o.common.thirdparty.freemarker.model;

import freemarker.template.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;



public class Null2ModelMapProxy<K,V> implements Map<K,V> , java.io.Serializable ,TemplateHashModel, TemplateScalarModel{

	private static final long serialVersionUID = 2189052146469439505L;
	
	private Map<K,V> map;

	public Map<K,V> getMap() {
		return map;
	}

	public void setMap(Map<K,V> map) {
		this.map = map;
	}
	
	
	public Null2ModelMapProxy() {}
	
	public Null2ModelMapProxy( Map<K,V> map ) {
		this.setMap(map);
	}

	public void clear() {
		this.map.clear();		
	}

	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		return this.map.entrySet();
	}

	public V get(Object key) {
		return this.map.get(key);
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public Set<K> keySet() {
		return this.map.keySet();
	}

	public V put(K key, V value) {
		return this.map.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		this.map.putAll(m);
	}

	public V remove(Object key) {
		return this.map.remove(key);
	}

	public int size() {
		return this.map.size();
	}

	public Collection<V> values() {
		return this.map.values();
	}
	
	 
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public TemplateModel get(String key) throws TemplateModelException { 
		
		Object v = this.map.get(key);	
		
		if( v == null ) {
			return NullModel.NULLMODEL;
		} else if( ( v instanceof Map )&& !(v instanceof TemplateModel) ) {
			return new Null2ModelMapProxy((Map)v);
		} else {
			return new DefaultObjectWrapper().wrap(v);
		}
	}
	

	public String getAsString() throws TemplateModelException {		
		return this.map.toString();
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Null2ModelMapProxy<?, ?> that = (Null2ModelMapProxy<?, ?>) o;

        return new EqualsBuilder()
                .append(map, that.map)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(map)
                .toHashCode();
    }

    @Override
	public String toString() {		
		return String.valueOf(this.map);
	}
	
	
}
