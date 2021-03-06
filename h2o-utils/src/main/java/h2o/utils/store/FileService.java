package h2o.utils.store;


import h2o.common.result.Response;
import h2o.common.result.TransResult;


public interface FileService {

    String createFileId();

    TransResult<FileMeta> getFileMeta(String bucket, String fileId);

    TransResult<FileObject> getFile(String bucket, String fileId);

    Response putFile(String bucket, String fileId, FileObject file);

    Response putFile(String bucket, String fileId, FileSource source);

    void output(String bucket, String fileId, FileConsumer consumer);

    Response delFile(String bucket, String fileId);

}
