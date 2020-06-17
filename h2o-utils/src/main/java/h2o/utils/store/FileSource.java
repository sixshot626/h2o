package h2o.utils.store;

import h2o.jodd.io.StreamUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileSource implements Closeable {

    private final InputStream inputStream;

    private String contentType;

    private Map<String, String> extInfo;


    public FileSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public FileSource(InputStream inputStream, String contentType) {
        this.inputStream = inputStream;
        this.contentType = contentType;
    }

    public void putInfo(String key , String value ) {
        if ( extInfo == null ) {
            extInfo = new HashMap<String, String>();
        }
        extInfo.put( key , value );
    }


    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public void close() throws IOException {
        StreamUtil.close(inputStream);
    }

    @Override
    public String toString() {
        return "FileSource{" +
                "contentType='" + contentType + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
