package h2o.utils.store;

import h2o.common.lang.LTimestamp;

import java.util.Map;

public class FileMeta implements java.io.Serializable {

    private static final long serialVersionUID = -7175322464671662175L;

    private final String bucketName;

    private final String name;

    private final long size;

    private final String contentType;

    private final LTimestamp createTime;

    private final Map<String, String> extInfo;



    public FileMeta(String bucketName, String name, long size, String contentType, LTimestamp createTime, Map<String, String> extInfo) {
        this.bucketName = bucketName;
        this.name = name;
        this.size = size;
        this.contentType = contentType;
        this.createTime = createTime;
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
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append(", extInfo=").append(extInfo);
        sb.append('}');
        return sb.toString();
    }
}
