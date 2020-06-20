package h2o.utils.store;



import h2o.common.result.TransResponse;
import h2o.common.result.TransStatus;
import h2o.common.result.TriState;


public interface FileService {

    String createFileId();

    TransResponse<TriState, FileMeta> getFileMeta(String bucket, String fileId);

    TransResponse<TriState, FileObject> getFile(String bucket, String fileId);

    TransStatus<TriState> putFile(String bucket, String fileId, FileObject file);

    TransStatus<TriState> putFile(String bucket, String fileId, FileSource source);

    void output(String bucket, String fileId, FileConsumer consumer);

    TransStatus<TriState> delFile(String bucket, String fileId);

}
