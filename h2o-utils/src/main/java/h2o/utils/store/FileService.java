package h2o.utils.store;


import h2o.common.result.ErrorMessage;
import h2o.common.result.Result;


public interface FileService {

    String createFileId();

    Result<ErrorMessage,FileMeta> getFileMeta(String bucket, String fileId);

    Result<ErrorMessage,FileObject> getFile(String bucket, String fileId);

    Result<ErrorMessage,Void> putFile(String bucket, String fileId, FileObject file);

    Result<ErrorMessage,Void> putFile(String bucket, String fileId, FileSource source);

    void output(String bucket, String fileId, FileConsumer consumer);

    Result<ErrorMessage,Void> delFile(String bucket, String fileId);

}
