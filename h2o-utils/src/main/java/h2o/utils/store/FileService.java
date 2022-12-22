package h2o.utils.store;


import h2o.common.result.CallResult;
import h2o.common.result.ErrorMessage;


public interface FileService {

    String createFileId();

    CallResult<ErrorMessage,FileMeta> getFileMeta(String bucket, String fileId);

    CallResult<ErrorMessage,FileObject> getFile(String bucket, String fileId);

    CallResult<ErrorMessage,Void> putFile(String bucket, String fileId, FileObject file);

    CallResult<ErrorMessage,Void> putFile(String bucket, String fileId, FileSource source);

    void output(String bucket, String fileId, FileConsumer consumer);

    CallResult<ErrorMessage,Void> delFile(String bucket, String fileId);

}
