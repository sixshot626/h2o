package h2o.utils.store;

import java.util.HashMap;
import java.util.Map;

public class FileObject implements java.io.Serializable {

    private final byte[] fileContent;

    private String contentType;

    private Map<String, String> extInfo;


    public FileObject( byte[] fileContent ) {
        this.fileContent = fileContent;
    }

    public FileObject( byte[] fileContent, String contentType) {
        this.fileContent = fileContent;
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
    public String toString() {
        return "FileObject{" +
                "contentType='" + contentType + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
