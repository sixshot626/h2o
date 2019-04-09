package h2o.common.util.json;

import h2o.common.util.bean.Beanfilter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JsonUtil {


	public String toJson( Object obj , Map<String,String[][]> filter ) {
		Object o = Beanfilter.filter( obj , filter );
		return toJson(o);
	}
	
	public String toJson( Object obj , Beanfilter bf , Map<String,String[][]> filter ) {
		Object o = bf.filterObject( obj , filter );
		return toJson(o);
	}

	public String toJson( Object obj ) {
		if (obj.getClass().isArray() || obj instanceof Collection) {
			return JSONArray.fromObject(obj).toString();
		} else {
			return JSONObject.fromObject(obj).toString();
		}
	}

	@SuppressWarnings("rawtypes")
	public Map json2Map(String json) {
		return JSONObject.fromObject(json);
	}
	
	@SuppressWarnings("rawtypes")
	public static List json2List(String json) {
		return JSONArray.fromObject(json);
	}
	

	public Object json2Object( String json ) {
		if( json == null ) return null;
		if( json.trim().startsWith("[") ) {
			return json2List(json);
		} else {
			return json2Map(json);
		}
	}

}
