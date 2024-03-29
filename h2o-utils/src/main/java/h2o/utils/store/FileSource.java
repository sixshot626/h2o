package h2o.utils.store;


import h2o.common.lang.SNumber;
import h2o.common.util.io.StreamUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileSource implements Closeable {

    private final InputStream inputStream;

    private final long objectSize;

    private final SNumber partSize;

    private String contentType;

    private Map<String, String> extInfo;


    public FileSource( InputStream inputStream, long objectSize, long partSize ) {
        this.inputStream = inputStream;
        this.objectSize = objectSize;
        this.partSize = new SNumber(partSize);
    }

    public FileSource( InputStream inputStream, String contentType, long objectSize, long partSize ) {
        this.inputStream = inputStream;
        this.objectSize = objectSize;
        this.partSize = new SNumber(partSize);;
        this.contentType = contentType;
    }

    public FileSource( InputStream inputStream, long objectSize ) {
        this.inputStream = inputStream;
        this.objectSize = objectSize;
        this.partSize = new SNumber();
    }

    public FileSource( InputStream inputStream, String contentType, long objectSize ) {
        this.inputStream = inputStream;
        this.contentType = contentType;
        this.objectSize = objectSize;
        this.partSize = new SNumber();
    }

    public void putInfo(String key , String value ) {
        if ( extInfo == null ) {
            extInfo = new HashMap<>();
        }
        extInfo.put( key , value );
    }


    public InputStream getInputStream() {
        return inputStream;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public SNumber getPartSize() {
        return partSize;
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
        final StringBuilder sb = new StringBuilder("FileSource{");
        sb.append("objectSize=").append(objectSize);
        sb.append(", partSize=").append(partSize);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", extInfo=").append(extInfo);
        sb.append('}');
        return sb.toString();
    }
}
