package h2o.common.util.web;

import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;

import javax.servlet.ServletRequest;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

public class ParameterUtil implements Map<String, Object> {

    private final Map<String, Object> parameters = MapBuilder.newMap();

    public ParameterUtil(ServletRequest req) {
        this.preSet(req);
        this.parseParameters(req);
    }

    protected void preSet(ServletRequest req) {
    }

    protected void initPutParameterValues(ServletRequest req, String pn) {
        String[] vals = req.getParameterValues(pn);
        this.initPut(pn, vals);
    }

    protected void initPut(String pn, Object val) {
        String[] vals = (String[]) val;
        if (vals.length == 1) {
            this.parameters.put(pn, vals[0]);
        } else {
            this.parameters.put(pn, new ListBuilder<String>().add(vals).get());
        }
    }

    private void parseParameters(ServletRequest req) {

        @SuppressWarnings("rawtypes")
        Enumeration pnames = req.getParameterNames();

        while (pnames.hasMoreElements()) {
            String pn = String.valueOf(pnames.nextElement());
            this.initPutParameterValues(req, pn);
        }

    }

    ;


    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String pname) {
        return (T) this.parameters.get(pname);
    }

    @Override
    public int size() {
        return parameters.size();
    }

    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return parameters.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return parameters.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return parameters.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return parameters.keySet();
    }

    @Override
    public Collection<Object> values() {
        return parameters.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return parameters.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if ( o instanceof ParameterUtil ) {
            ParameterUtil parameterUtil = (ParameterUtil) o;
            return this.parameters.equals(parameterUtil.parameters);
        } else {
            return parameters.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    @Override
    public String toString() {
        return this.parameters.toString();
    }


}
