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
        final StringBuilder sb = new StringBuilder("FileMeta{");
        sb.append("bucketName='").append(bucketName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", size=").append(size);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", extInfo=").append(extInfo);
        sb.append('}');
        return sb.toString();
    }
}
