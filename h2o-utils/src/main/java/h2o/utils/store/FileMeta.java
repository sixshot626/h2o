package h2o.utils.store;

import java.util.Map;

public class FileMeta implements java.io.Serializable {

    private final String bucketName;

    private final String name;

    private final long size;

    private final String contentType;

    private final Map<String, String> extInfo;

    public FileMeta(String bucketName, String name, long size, String contentType, Map<String, String> extInfo) {
        this.bucketName = bucketName;
        this.name = name;
        this.size = size;
        this.contentType = contentType;
        this.extInfo = extInfo;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "bucketName='" + bucketName + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", contentType='" + contentType + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
