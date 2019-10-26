package h2o.common.util.web;

import h2o.common.exception.ExceptionUtil;
import h2o.common.util.io.CharsetWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger( HttpClient.class.getName() );

    private final CloseableHttpClient client;


	private volatile String contentType;

    private volatile CharsetWrapper sendCharset = new CharsetWrapper("UTF-8");
	
	private volatile CharsetWrapper charset = new CharsetWrapper("UTF-8");

	private volatile HttpEchoCallback callback = null;


    public HttpClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpClient setSendCharset(String sendCharset) {
        this.sendCharset = new CharsetWrapper(sendCharset);
        return this;
    }

    public HttpClient setCharset(String charset) {
        this.charset = new CharsetWrapper(charset);
        return this;
    }

    public HttpClient setCallback(HttpEchoCallback callback) {
        this.callback = callback;
        return this;
    }


    public HttpClient() {
        this.client = HttpClients.createDefault();
    }


    public HttpClient( CloseableHttpClient client ) {
        this.client = client;
    }






    public  String get(URI uri) {

        HttpGet httpget = new HttpGet(uri);

        return echo( httpget );

    }

    public  String get(String url) {

        HttpGet httpget = new HttpGet(url);

        return echo( httpget );

    }



    public String post(URI uri) {

        return post( uri , (Map<String,String>)null  );
    }

    public String post(String url) {

        return post( url , (Map<String,String>)null );

    }



    public  String post(URI uri , Map<String,String> para ) {

        HttpPost httppost = new HttpPost(uri);

        return post(httppost , para );

    }

    public  String post(String url , Map<String,String> para ) {

        HttpPost httppost = new HttpPost(url);

        return post(httppost , para );

    }




    public String post(HttpPost httppost , Map<String,String> para ) {

        try {

            HttpEntity entity = null;

            if(para != null && !para.isEmpty()) {
                entity = new UrlEncodedFormEntity(HttpClientUtil.para2nvList(para) , sendCharset.charset );
            }

            return post( httppost , entity );

        } catch( Exception e ) {

            log.debug("echoPost",e);
            throw ExceptionUtil.toRuntimeException(e);

        }


    }





    public String post(URI uri , String data ) {

        HttpEntity entity = StringUtils.isBlank(contentType) ? new StringEntity(data , charset.charset) : new StringEntity(data , ContentType.create(contentType, charset.charset));

        return post(uri , entity);

    }

    public String post(String url , String data ) {

        HttpEntity entity = StringUtils.isBlank(contentType) ? new StringEntity(data , charset.charset) : new StringEntity(data , ContentType.create(contentType, charset.charset));

        return post(url , entity);

    }



    public String post(URI uri , HttpEntity entity ) {

        HttpPost httppost = new HttpPost(uri);

        return post(httppost , entity);

    }

    public String post(String url , HttpEntity entity) {

        HttpPost httppost = new HttpPost(url);

        return post(httppost , entity);

    }




    public String post(HttpPost httppost, HttpEntity entity ) {

        if (entity != null) {
            httppost.setEntity(entity);
        }

        return echo(httppost);
    }




    public String echo(HttpUriRequest request) {

        return HttpClientUtil.echo( client , false , request , charset , callback );

    }


    public void close() {

        if ( client != null ) try {

            client.close();

        } catch (IOException e) {
            log.debug( "", e );
        }
    }
	

}
