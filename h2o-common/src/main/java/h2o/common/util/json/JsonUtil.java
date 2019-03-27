package h2o.common.util.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import h2o.common.util.web.Beanfilter;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class JsonUtil {

	private final GsonCreater creater;

	public JsonUtil() {
		this.creater = new GsonCreater() {
			@Override
			public Gson create() {
				return new Gson();
			}
		};
	}

	public JsonUtil( GsonCreater creater ) {
		this.creater = creater;
	}

	public String toJson(Object obj , Map<String,String[][]> filter ) {
		Object o = Beanfilter.filter( obj , filter );
		return toJson(o);
	}
	
	public String toJson( Object obj , Beanfilter bf , Map<String,String[][]> filter ) {
		Object o = bf.filterObject( obj , filter );
		return toJson(o);
	}

	public String toJson( Object obj ) {
		return this.creater.create().toJson( obj );
	}

	@SuppressWarnings("rawtypes")
	public Map json2Map(String json) {

	    if( StringUtils.isBlank( json ) ) return null;

		return this.creater.create().fromJson( json , new TypeToken<Map>() {}.getType()  );
	}
	
	@SuppressWarnings("rawtypes")
	public List json2List(String json) {

        if( StringUtils.isBlank( json ) ) return null;

		return this.creater.create().fromJson( json , new TypeToken<List>() {}.getType() );

	}
	

	public Object json2Object( String json ) {

        if( StringUtils.isBlank( json ) ) return null;

		if( json.trim().startsWith("[") ) {
			return json2List(json);
		} else {
			return json2Map(json);
		}
	}

	public <T> T json2Object( String json , Class<T> clazz ) {

		if( StringUtils.isBlank( json ) ) return null;

		return null;

	}

	public static void main( String[] args ) {

		Object obj = new JsonUtil().json2List( "[1,2]");
		System.out.println( obj.getClass() );
	}

}
