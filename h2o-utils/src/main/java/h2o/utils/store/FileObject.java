package h2o.utils.store;

import h2o.common.lang.LTimestamp;

import java.util.HashMap;
import java.util.Map;

public class FileObject implements java.io.Serializable {

    public static final long MAX_PART_SIZE = 5L * 1024 * 1024 * 1024;

    private final byte[] fileContent;

    private final long objectSize;

    private final long partSize;

    private String contentType;

    private LTimestamp createTime;

    private Map<String, String> extInfo;

    public FileObject( byte[] fileContent , long partSize) {
        this.fileContent = fileContent;
        this.objectSize = fileContent.length;
        this.partSize = partSize;
    }

    public FileObject( byte[] fileContent, String contentType , long partSize) {
        this.fileContent = fileContent;
        this.objectSize = fileContent.length;
        this.partSize = partSize;
        this.contentType = contentType;
    }

    public FileObject( byte[] fileContent) {
        this.fileContent = fileContent;
        this.objectSize = fileContent.length;
        this.partSize = MAX_PART_SIZE;
    }

    public FileObject( byte[] fileContent, String contentType) {
        this.fileContent = fileContent;
        this.objectSize = fileContent.length;
        this.partSize = MAX_PART_SIZE;
        this.contentType = contentType;
    }

    public void putInfo(String key , String value ) {
        if ( extInfo == null ) {
            extInfo = new HashMap<String,String>();
        }
        extInfo.put( key , value );
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public long getPartSize() {
        return partSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LTimestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LTimestamp createTime) {
        this.createTime = createTime;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileObject{");
        sb.append("objectSize=").append(objectSize);
        sb.append(", partSize=").append(partSize);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", extInfo=").append(extInfo);
        sb.append('}');
        return sb.toString();
    }
}
