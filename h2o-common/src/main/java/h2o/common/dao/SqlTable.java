package h2o.common.dao;

import h2o.common.concurrent.LockMap;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.collection.MapBuilder;
import h2o.common.util.io.StreamUtil;
import h2o.common.util.lang.RuntimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class SqlTable {


    private static final Logger log = LoggerFactory.getLogger( SqlTable.class.getName() );
	
	
	private final Map<String,Map<String,String>> classNameSqlMap = new java.util.concurrent.ConcurrentHashMap<String,Map<String,String>>();
	
	private final LockMap lockm = new LockMap();	
	
	private volatile boolean cache = true;
	

	private volatile String path_prefix = "";
	private volatile String extName = ".sql";
	private volatile String default_path;
	private volatile String confFileName;
	
	private volatile String realPath;
	private volatile String runClassName;
	
	private volatile TemplateUtil templateUtil = new TemplateUtil();
	private final Map<String, Object> templateData = MapBuilder.newConcurrentHashMap();
	
	
	
	
	public SqlTable() {}
	
	public SqlTable( boolean cache ) {
		this.cache = cache;
	}
	
	private SqlTable( SqlTable st ) {
		this.path_prefix 	=  st.path_prefix;
		this.extName 		=  st.extName;
		this.default_path 	=  st.default_path;
		this.confFileName 	=  st.confFileName;
		this.templateUtil	=  st.templateUtil;
		this.templateData.putAll( st.templateData );
	}
	
	public SqlTable setCache(boolean cache) {
		this.cache = cache;
		return this;
	}

	public SqlTable setTemplateData(Map<String, ?> data) {
		this.templateData.putAll(data);
		return this;
	}
	
	public SqlTable putData( String k , Object v ) {
		this.templateData.put( k , v );
		return this;
	}

	public SqlTable setPath_prefix(String path_prefix) {
		this.path_prefix = path_prefix;
		return this;
	}

	public SqlTable setExtName(String extName) {
		this.extName = extName;
		return this;
	}

	public SqlTable setDefault_path(String default_path) {
		this.default_path = default_path;
		return this;
	}
	
	public SqlTable setConfFileName(String confFileName) {
		this.confFileName = confFileName;
		return this;
	}

	public SqlTable setRealPath(String realPath) {
		this.realPath = realPath;
		return this;
	}
	
	public SqlTable setRunClass(Class<?> runClass) {
		this.runClassName = runClass.getName();
		return this;
	}

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    public void setTemplateUtil(TemplateUtil templateUtil) {
		this.templateUtil = templateUtil;
	}

	protected Map<String,String> loadSqlTable( String path ) {
		
		String sqlConfig = StreamUtil.readFileContent(path, "UTF-8");
		
		String[] sqlConfigs = StringUtils.substringsBetween(sqlConfig, ":{", "};");
		
		Map<String,String> sqlTable = new java.util.concurrent.ConcurrentHashMap<String,String>();
		
		for( String ss : sqlConfigs ) {
			String key = StringUtils.substringBefore(ss, "=").trim();
			String sql = StringUtils.substringAfter(ss, "=");
			sqlTable.put(key, sql);
		}
		
		log.debug("Load sqlTable from path:{}\n{}",path , sqlTable);
		
		return sqlTable;
	}
	
	
	
	private String convertPath( String path ) {
	
		if( this.realPath == null ) {
		
			if( path == null ) {		
				String className = this.runClassName == null ? RuntimeUtil.getCallClassName(SqlTable.class.getName()) : this.runClassName ;	
				if( className == null ) {
					if( this.default_path == null ) {
						throw new RuntimeException("Get class path Exception ");
					} else {
						path = this.default_path;
					}
				} else {
					
					path = StringUtils.replace( StringUtils.substringBefore(className, "$") , ".", "/");
					
				}
			} 
			
	
			if( StringUtils.isNotBlank(this.confFileName) ) {
				if( StringUtils.contains(path, "/") ) {
					path = StringUtils.substringBeforeLast(path, "/") + "/" + this.confFileName;
				} else {
					path = this.confFileName;
				}
			}		
			
			return path_prefix + path + extName ;	
		
		} else {
			
			return this.realPath;
		}
		
		
	}
	
	
	
	
	
	private void load( String path ) {		
		classNameSqlMap.put( path , loadSqlTable( path ) );
	}
	
	
	public SqlTable getSpecificSqlTable() {
		return this.getSpecificSqlTable(null);
	}
	
	public SqlTable getSpecificSqlTable( String path ) {
		
		SqlTable st = new SqlTable(this);
		st.realPath = st.convertPath(path);
		
		return st;
	}
	
	
	
	public Map<String,String> getSqlMap() {
		return this.getSqlMap(null);
	}
	
	public Map<String,String> getSqlMap( String path ) {
		
		path = convertPath(path);
		
		log.debug("Get sql from  path:{}" , path );
		
		Map<String,String> sqlMap;
		if( this.cache ) {
			
			sqlMap = classNameSqlMap.get(path);
			if( sqlMap == null) {		
				
				if( this.realPath == null ) {
					Lock lock = lockm.getLock(path, true);
					lock.lock();
					try {
						sqlMap = classNameSqlMap.get(path);
						if( sqlMap == null ) {
							load(path);
							sqlMap = classNameSqlMap.get(path);
						}				
						
					} finally {
						lock.unlock();
					}
				} else {
					load(path);
					sqlMap = classNameSqlMap.get(path);
				}
				
			}
		} else {
			sqlMap = this.loadSqlTable(path);
		}
		
		if( sqlMap == null) {
			throw new RuntimeException("No serach sql config in path:" + path  ) ;
		}
		
		return sqlMap;
	
	}
	
	public String getSql( String queryName ) {		
		return this.getSql( null, queryName );		
	}
	
	
	public String getSql( String path, String queryName ) {		
		String sql = this.getSqlMap( path ).get( queryName );
		log.debug("Get [{}] sql :{}", queryName ,  sql );
		return sql;
	}
	

	
	public String getSqlT( String queryName ) {
		return this.getSqlT(queryName, this.templateData);
	}
	
	public String getSqlT( String path, String queryName ) {
		return this.getSqlT( path, queryName, this.templateData);
	}
	
	public String getSqlT( String queryName , Map<String, ?> data ) {
		return this.getSqlT( null , queryName, data);
	}
	
	public String getSqlT( String path, String queryName , Map<String, ?> data ) {
		
		String st = this.getSql( path, queryName);
		if( StringUtils.isBlank(st) ) {
			return st;
		}
		
		Map<String, Object> dataTmp = MapBuilder.newMap();
		dataTmp.putAll(this.templateData);
		dataTmp.putAll(data);
		
		String sql = this.templateUtil.process(dataTmp, st);
		
		log.debug("Get [{}] sql T :{}", queryName , sql );
		
		return sql;
	}
	
	
	
	
}
