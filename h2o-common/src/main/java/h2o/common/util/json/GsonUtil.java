package h2o.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class GsonUtil {

	private final GsonCreater creater;

	public GsonUtil() {

		this.creater = new GsonCreater() {

			private final Gson _gson = new GsonBuilder().disableHtmlEscaping().create();

			@Override
			public Gson create() {
				return _gson;
			}
		};

	}

	public GsonUtil( GsonCreater creater ) {
		this.creater = creater;
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

		return this.creater.create().fromJson( json , clazz );

	}



}
