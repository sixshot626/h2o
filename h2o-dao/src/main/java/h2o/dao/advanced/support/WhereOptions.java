package h2o.dao.advanced.support;

import h2o.common.lang.Key;
import h2o.common.lang.Special;
import h2o.dao.exception.DaoException;
import h2o.dao.structure.TableStruct;

import java.util.*;
import java.util.function.Function;

public final class WhereOptions implements WhereConditions {


    private final TableStruct tableStruct;

    private final boolean allowUnconditional;

    public WhereOptions(TableStruct tableStruct , boolean allowUnconditional) {
        this.tableStruct = tableStruct;
        this.allowUnconditional = allowUnconditional;
    }

    public boolean unconditional;

    public List wattr;

    public String where;

    public Map wargs;


    public boolean isSetted() {
        return this.unconditional || this.where != null || this.wattr != null || this.wargs != null;
    }


    public void setUnconditional(boolean unconditional) {
        if (isSetted()) {
            throw new DaoException("'where' is setted");
        }
        this.unconditional = unconditional;
    }

    public void setWhere(String where) {
        if (isSetted()) {
            throw new DaoException("'where' is setted");
        }
        this.where = where;
    }

    public void setWattr(Object[] wattrs) {
        if (isSetted()) {
            throw new DaoException("'where' is setted");
        }
        this.wattr = args2List(wattrs);
    }

    public void setWargs(Object[] wargs) {

        if (isSetted()) {
            throw new DaoException("'where' is setted");
        }

        if (wargs == null) {
            this.wargs = Collections.EMPTY_MAP;
        } else {

            Map m = new HashMap<>();
            for ( int i = 0, len = wargs.length; i < len; i++) {
                if (wargs[i] instanceof Special && ( i + 1 == len || wargs[i + 1] != null ) ) {
                    m.put(wargs[i], null);
                } else if (wargs[i] instanceof Map) {
                    m.putAll( (Map)wargs[i] );
                } else {
                    m.put(wargs[i], wargs[++i]);
                }
            }
            this.wargs = Collections.unmodifiableMap(m);

        }
    } 
    
    
    
    

    
    @Override
    public String whereSql() {

        if ( this.where != null) {
            return str(" \nwhere   ",  this.where);
        } else if ( this.wattr != null) {
            return str(" \n", buildWhere( this.wattr));
        } else if ( this.wargs != null) {
            if (  this.wargs.isEmpty() ) {
                if (allowUnconditional) {
                    return null;
                } else {
                    throw new DaoException("'where' is not set");
                }
            }
            return str(" \n", buildWhereW( this.wargs.keySet()));
        } else if ( this.unconditional) {
            return null;
        } else {
            throw new DaoException("'where' is not set");
        }

    }

    @Override
    public Map<Object, Object> params() {

        Map<Object,Object> para = Collections.emptyMap();

        if (this.where != null) {
            return para;
        } else if (this.wattr != null) {
            return para;
        } else if (this.wargs != null) {
            if (this.wargs.isEmpty()) {
                return para;
            } else {
                return convWArgs(this.wargs);
            }
        } else if (this.unconditional) {
            return para;
        } else {
            throw new DaoException("'where' is not set");
        }
    }


    private Map<Object, Object> convWArgs(Map<?, ?> wargs) {
        Map<Object, Object> nmap = new HashMap<>();
        for (Map.Entry<?, ?> entry : wargs.entrySet()) {
            if ( entry.getKey() instanceof Special) {
                continue;
            }
            nmap.put(str("w__", name(entry.getKey())), entry.getValue());
        }
        return nmap;
    }






    private String buildWhereF(Function<?, String> f, Collection<?> ws) {

        if (ws == null || ws.isEmpty()) {
            throw new DaoException(String.valueOf(ws));
        }

        StringBuilder sqlWhere = new StringBuilder("where   ");
        int i = 0;
        for (Object k : ws) {
            if (i++ > 0) {
                sqlWhere.append("\n    and ");
            }
            if (k instanceof Special) {
                sqlWhere.append(((Special) k).getValue());
            } else {
                sqlWhere.append(column(k));
                sqlWhere.append(" = ");
                sqlWhere.append(((Function<Object, String>) f).apply(k));
            }
        }

        return sqlWhere.toString();

    }

    private String buildWhere(Collection<?> ws) {
        return buildWhereF(k -> str(":", name(k)), ws);
    }

    private String buildWhereW(Collection<?> ws) {
        return buildWhereF(k -> str(":w__", name(k)), ws);
    }




    private static List args2List(Object[] args) {
        if (args == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(Arrays.asList(args));
    }


    private String str(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            if (s != null) {
                sb.append(name(s));
            }
        }
        return sb.toString();
    }

    private String name(Object obj) {
        String key;
        if (obj instanceof String) {
            key = (String) obj;
        } else if (obj instanceof Key) {
            key = ((Key) obj).name();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return key;
    }


    private String column(Object attr) {
        return this.tableStruct.getColumn(attr).colName;
    }



}
