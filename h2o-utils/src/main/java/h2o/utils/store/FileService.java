package h2o.utils.store;


import h2o.common.result.CallResult;
import h2o.common.result.ErrorMsg;
import h2o.common.result.StatusMsg;


public interface FileService {

    String createFileId();

    CallResult<ErrorMsg,FileMeta> getFileMeta(String bucket, String fileId);

    CallResult<ErrorMsg,FileObject> getFile(String bucket, String fileId);

    CallResult<ErrorMsg,Void> putFile(String bucket, String fileId, FileObject file);

    CallResult<ErrorMsg,Void> putFile(String bucket, String fileId, FileSource source);

    void output(String bucket, String fileId, FileConsumer consumer);

    CallResult<ErrorMsg,Void> delFile(String bucket, String fileId);

}
