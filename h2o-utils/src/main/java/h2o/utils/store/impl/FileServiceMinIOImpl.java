package h2o.utils.store.impl;



import h2o.common.Tools;
import h2o.common.cluster.ClusterUtil;
import h2o.common.collections.builder.MapBuilder;
import h2o.common.exception.ExceptionUtil;
import h2o.common.result.TransResponse;
import h2o.common.result.TransReturn;
import h2o.common.result.TransStatus;
import h2o.common.result.TriState;
import h2o.common.util.date.DateUtil;
import h2o.jodd.io.StreamUtil;
import h2o.utils.store.*;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FileServiceMinIOImpl implements FileService {

    private static final String EXT_INFO_PREFIX = "x-amz-meta-";

    private final MinioClient mc;

    public FileServiceMinIOImpl(MinioClient mc) {
        this.mc = mc;
    }

    public FileServiceMinIOImpl(String endpoint, String accessKey, String secretKey) {
        try {
            this.mc = new MinioClient( endpoint , accessKey , secretKey );
        } catch ( Exception e ) {
            throw ExceptionUtil.toRuntimeException( e );
        }
    }

    private final ClusterUtil.IdGenerator idGenerator = new ClusterUtil.IdGenerator();

    @Override
    public String createFileId() {
        Date date = new Date();
        return DateUtil.toString( new Date() , "yyyyMMdd" ) + "/FS" +  DateUtil.toString( new Date() , "HHmmss" )  + idGenerator.makeId();
    }

    @Override
    public TransStatus<TriState> putFile(String bucket , String fileId , FileObject file  ) {

        try {

            if ( ! mc.bucketExists( bucket ) ) {
                mc.makeBucket( bucket );
            }

            PutObjectOptions options = new PutObjectOptions(file.getObjectSize(),file.getPartSize());
            options.setContentType( file.getContentType() );
            options.setHeaders( file.getExtInfo() );

            mc.putObject( bucket , fileId ,
                    new ByteArrayInputStream( file.getFileContent() ), options );


            return new TransReturn<TriState, Object>().setStatus( TriState.Success ).setSuccess(true);

        } catch ( Exception e ) {
            Tools.log.error(e);
            return new TransReturn<TriState, Object>().setStatus( TriState.Failure ).setSuccess(false);
        }


    }

    @Override
    public TransStatus<TriState> putFile(String bucket, String fileId, FileSource source ) {
        try {

            if ( ! mc.bucketExists( bucket ) ) {
                mc.makeBucket( bucket );
            }

            PutObjectOptions options = new PutObjectOptions( source.getObjectSize(), source.getPartSize() );
            options.setContentType( source.getContentType() );
            options.setHeaders( source.getExtInfo() );

            mc.putObject( bucket , fileId ,
                    source.getInputStream() , options  );


            return new TransReturn<TriState, Object>().setStatus( TriState.Success ).setSuccess(true);

        } catch ( Exception e ) {

            Tools.log.error(e);
            return new TransReturn<TriState, Object>().setStatus( TriState.Failure ).setSuccess(false);

        } finally {
            StreamUtil.close( source );
        }
    }



    @Override
    public TransResponse<TriState, FileMeta> getFileMeta(String bucket, String fileId) {

        try {

            ObjectStat stat = mc.statObject(bucket, fileId);

            FileMeta meta = new FileMeta( stat.bucketName() , stat.name() ,
                     stat.length() , stat.contentType() ,
                    parseExtInfo( stat.httpHeaders() ) );

            return new TransReturn<TriState, FileMeta>().setResult(meta).setStatus( TriState.Success ).setSuccess(true);

        } catch ( Exception e ) {
            Tools.log.error(e);
            return new TransReturn<TriState, FileMeta>().setStatus( TriState.Failure ).setSuccess(false);
        }

    }

    @Override
    public TransResponse<TriState, FileObject> getFile(String bucket , String fileId ) {

        BufferedInputStream fileIn = null;
        try {

            fileIn = new BufferedInputStream( mc.getObject( bucket , fileId) );

            byte[] fileContent = StreamUtil.readBytes(fileIn);
            FileObject fileObject = new FileObject( fileContent );

            ObjectStat stat = mc.statObject(bucket, fileId);

            fileObject.setContentType( stat.contentType() );
            fileObject.setExtInfo( parseExtInfo( stat.httpHeaders() ) );

            return new TransReturn<TriState, FileObject>().setResult( fileObject ).setStatus( TriState.Success ).setSuccess(true);

        } catch ( Exception e ) {

            Tools.log.error(e);
            return new TransReturn<TriState, FileObject>().setStatus( TriState.Failure ).setSuccess(false);

        } finally {
            StreamUtil.close( fileIn );
        }
    }



    @Override
    public void output(String bucket, String fileId, FileConsumer consumer ) {

        FileSource source = null;
        try {



            ObjectStat stat = mc.statObject(bucket, fileId);

            source = new FileSource( mc.getObject( bucket , fileId) , stat.length() );

            source.setContentType( stat.contentType() );
            source.setExtInfo( parseExtInfo( stat.httpHeaders() ) );

            consumer.accept( source );

        } catch ( Exception e ) {
            throw ExceptionUtil.toRuntimeException( e );
        } finally {
            StreamUtil.close( source );
        }
    }


    private Map<String, String> parseExtInfo(Map<String, List<String>> headers ) {

        Map<String, String> extInfo = MapBuilder.newMap();

        for ( Map.Entry<String, List<String>> h : headers.entrySet() ) {
            if ( h.getKey().startsWith( EXT_INFO_PREFIX ) ) {
                extInfo.put(StringUtils.substringAfter( h.getKey() , EXT_INFO_PREFIX ) , h.getValue().get(0) );
            }
        }

        return extInfo;

    }





    @Override
    public TransStatus<TriState> delFile(String bucket, String fileId) {

        try {

            mc.removeObject( bucket , fileId );

            return new TransReturn<TriState, Object>().setStatus( TriState.Success ).setSuccess(true);

        } catch ( Exception e ) {
            Tools.log.error(e);
            return new TransReturn<TriState, Object>().setStatus( TriState.Failure ).setSuccess(false);
        }

    }
}
