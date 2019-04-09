package h2o.common.web;

import h2o.common.collections.builder.MapBuilder;
import h2o.common.exception.ExceptionUtil;
import h2o.common.util.json.JsonUtil;
import h2o.common.util.bean.Beanfilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;



public class WebTools {

    private static final Logger log = LoggerFactory.getLogger( WebTools.class.getName() );

    private volatile Beanfilter bf;

	private volatile JsonUtil jsonUtil;
	
	public WebTools setBeanfilter(Beanfilter beanfilter) {
		this.bf = beanfilter;
		return this;
	}
	
	private volatile String outCharacterEncoding;
	public WebTools setOutCharacterEncoding( String enc ) {
		outCharacterEncoding = enc;
		return this;
	}


	public WebTools() {}
	
	public WebTools( String enc ) {
		outCharacterEncoding = enc;
	}

	
	public String state( String s ) {
		if (s == null) {
			return "0";
		} else {
			return "msg" + s;
		}
	}
	
	public String jsonState(  Object data ) {
		if ( data == null) {
			return jsonState( true, "OK");
		} else {
			return jsonState( false, data );
		}
	}
	
	public String jsonState(  boolean success, Object data ) {
		Map<String,Object> m = MapBuilder.newMap();
		m.put("success", success );
		m.put("ret", data);
		return jsonUtil.toJson(m);
	}
	
	
	public  void outJSON(HttpServletResponse response, String msg) {
		out(response, msg);
	}
	
	public  void outJSON(HttpServletResponse response, Object obj , Map<String,String[][]> filter ) {
		Object o = bf == null ? Beanfilter.filter( obj , filter ) : bf.filterObject( obj , filter );
		this.outJSON(response, o);
	}

	public  void outJSON(HttpServletResponse response, Object obj) {
		String msg = jsonUtil.toJson(obj);
		out(response, msg);
	}

	public  void out(HttpServletResponse response, String msg) {
		out(response, "text/html", msg);
	}

	public  void outState(HttpServletResponse response, String s) {		
		out(response, this.state(s));		
	}
	
	public  void outJsonState(HttpServletResponse response, Object data ) {
		out(response, this.jsonState( data ));	
	}
	
	public  void outJsonState(HttpServletResponse response, boolean success, Object data) {		
		out(response, this.jsonState(success,data));		
	}

	public  void out(HttpServletResponse response, String contentType, String msg) {
		response.setContentType(contentType);
		if( this.outCharacterEncoding != null ) {
			response.setCharacterEncoding(outCharacterEncoding);
		}
		try {
			response.getWriter().print(msg);
		} catch (Exception e) {
			log.debug("out", e);
			throw ExceptionUtil.toRuntimeException(e);
		}
	}


}
