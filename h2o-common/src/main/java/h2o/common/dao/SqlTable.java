package h2o.common.dao;

import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.io.StreamUtil;
import h2o.common.util.lang.RuntimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlTable {


    private static final Logger log = LoggerFactory.getLogger( SqlTable.class.getName() );
	
	
	private final Map<String,Map<String,String>> classNameSqlMap = new ConcurrentHashMap<>();
	

	private final boolean cache;

	private final String pathPrefix;
	private final String extName;
	private final String defaultPath;
	private final String confFileName;
	
	private final String realPath;
	private final String runClassName;
	
	private final TemplateUtil templateUtil;
	private final Map<String, Object> templateData = new ConcurrentHashMap<>();
	

	public static class Builder {

		public Builder() {}

		public Builder(boolean cache) {
			this.cache = cache;
		}

		private boolean cache = true;

		private String pathPrefix = "";
		private String extName = ".sql";
		private String defaultPath;
		private String confFileName;

		private String realPath;
		private String runClassName;


		private TemplateUtil templateUtil = new TemplateUtil();
		private Map<String, Object> templateData = new HashMap<>();



		public SqlTable build() {
			return new SqlTable( this );
		}


		public Builder setCache(boolean cache) {
			this.cache = cache;
			return this;
		}


		public Builder setTemplateUtil(TemplateUtil templateUtil) {
			this.templateUtil = templateUtil;
			return this;
		}

		public Builder setTemplateData(Map<String, ?> data) {
			this.templateData.putAll(data);
			return this;
		}

		public Builder putData(String k , Object v ) {
			this.templateData.put( k , v );
			return this;
		}

		public Builder setPathPrefix(String pathPrefix) {
			this.pathPrefix = pathPrefix;
			return this;
		}

		public Builder setExtName(String extName) {
			this.extName = extName;
			return this;
		}

		public Builder setDefaultPath(String defaultPath) {
			this.defaultPath = defaultPath;
			return this;
		}

		public Builder setConfFileName(String confFileName) {
			this.confFileName = confFileName;
			return this;
		}

		public Builder setRealPath(String realPath) {
			this.realPath = realPath;
			return this;
		}

		public Builder setRunClass(Class<?> runClass) {
			this.runClassName = runClass.getName();
			return this;
		}

		public Builder setRunClassName(String runClassName) {
			this.runClassName = runClassName;
			return this;
		}
	}





	
	
	public SqlTable() {
		this( new Builder() );
	}
	
	public SqlTable( boolean cache ) {
		this( new Builder(cache) );
	}

	public SqlTable( Builder builder ) {

		this.cache 			=  builder.cache;

		this.pathPrefix     =  builder.pathPrefix;
		this.extName 		=  builder.extName;
		this.defaultPath    =  builder.defaultPath;
		this.confFileName 	=  builder.confFileName;
		this.realPath       =  builder.realPath;
		this.runClassName   =  builder.runClassName;

		this.templateUtil	=  builder.templateUtil;
		this.templateData.putAll( builder.templateData );
	}


	public SqlTable putData( String k , Object v ) {
		this.templateData.put( k , v );
		return this;
	}

    public Map<String, Object> getTemplateData() {
        return templateData;
    }



	protected Map<String,String> loadSqlTable( String path ) {
		
		String sqlConfig = StreamUtil.readFileContent(path, "UTF-8");
		
		String[] sqlConfigs = StringUtils.substringsBetween(sqlConfig, ":{", "};");
		
		Map<String,String> sqlMap = new HashMap();
		
		for( String ss : sqlConfigs ) {
			String key = StringUtils.substringBefore(ss, "=").trim();
			String sql = StringUtils.substringAfter(ss, "=");
			sqlMap.put(key, sql);
		}
		
		log.debug("Load sqlTable from path:{}\n{}",path , sqlMap);
		
		return Collections.unmodifiableMap( sqlMap );
	}
	
	
	
	private String convertPath( String path ) {
	
		if( this.realPath == null ) {
		
			if( path == null ) {		
				String className = this.runClassName == null ? RuntimeUtil.getCallClassName(SqlTable.class.getName()) : this.runClassName ;	
				if( className == null ) {
					if( this.defaultPath == null ) {
						throw new RuntimeException("Get class path Exception ");
					} else {
						path = this.defaultPath;
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
			
			return pathPrefix + path + extName ;
		
		} else {
			
			return this.realPath;

		}
		
		
	}
	
	
	
	
	
	private void load( String path ) {		
		classNameSqlMap.putIfAbsent( path , loadSqlTable( path ) );
	}
	
	
	public SqlTable getSpecificSqlTable() {
		return this.getSpecificSqlTable(null);
	}
	
	public SqlTable getSpecificSqlTable( String path ) {

		Builder builder = new Builder();

		builder.setCache( this.cache );

		builder.setPathPrefix( this.pathPrefix );
		builder.setExtName( this.extName );
		builder.setDefaultPath( this.defaultPath );
		builder.setConfFileName( this.confFileName );
		builder.setRunClassName( this.runClassName );

		builder.setTemplateUtil( this.templateUtil );
		builder.setTemplateData( this.templateData );


		// Real path
		builder.setRealPath( this.convertPath( path ) );

		return builder.build();
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
				load(path);
				sqlMap = classNameSqlMap.get(path);
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
		
		Map<String, Object> dataTmp = new HashMap<>();
		dataTmp.putAll(this.templateData);
		dataTmp.putAll(data);
		
		String sql = this.templateUtil.process(dataTmp, st);
		
		log.debug("Get [{}] sql T :{}", queryName , sql );
		
		return sql;
	}
	

	
}
