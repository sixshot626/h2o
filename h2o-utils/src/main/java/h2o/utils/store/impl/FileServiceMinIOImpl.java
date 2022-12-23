package h2o.utils.store.impl;


import h2o.common.cluster.ClusterUtil;
import h2o.common.exception.ExceptionUtil;
import h2o.common.result.CallResult;
import h2o.common.result.ErrorMessage;
import h2o.common.result.ExceptionMessage;
import h2o.common.result.StatusMessage;
import h2o.common.util.date.DateUtil;
import h2o.common.util.io.StreamUtil;
import h2o.common.util.lang.StringUtil;
import h2o.utils.store.*;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;


public class FileServiceMinIOImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceMinIOImpl.class);

    private final MinioClient mc;

    public FileServiceMinIOImpl(MinioClient mc) {
        this.mc = mc;
    }

    public FileServiceMinIOImpl(String endpoint, String accessKey, String secretKey) {
        try {
            
            this.mc =  MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

        } catch ( Exception e ) {
            throw ExceptionUtil.toRuntimeException( e );
        }
    }

    private final ClusterUtil.IdGenerator idGenerator = new ClusterUtil.IdGenerator();

    @Override
    public String createFileId() {
        Date date = new Date();
        return DateUtil.toString( date , "yyyyMMdd" ) + "/FS" +  DateUtil.toString( date , "HHmmss" )  + idGenerator.makeId();
    }

    @Override
    public CallResult<ErrorMessage,Void> putFile(String bucket , String fileId , FileObject file  ) {

        try {

            if (!mc.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                mc.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            return CallResult.fail( new ExceptionMessage( e ) );
        }

        try {

            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder().bucket(bucket).object(fileId);


            if ( file.getContentType() != null ) {
                putObjectArgsBuilder.contentType(file.getContentType());
            }
            if ( file.getExtInfo() != null ) {
                putObjectArgsBuilder.extraHeaders(file.getExtInfo());
            }

            putObjectArgsBuilder.stream( new ByteArrayInputStream( file.getFileContent() ) , file.getObjectSize(),
                    file.getPartSize().isPresent() ? file.getPartSize().longValue() : ObjectWriteArgs.MAX_PART_SIZE );


            mc.putObject( putObjectArgsBuilder.build() );


            return CallResult.success();

        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            return CallResult.fail( new ExceptionMessage( e ) );
        }


    }

    @Override
    public CallResult<ErrorMessage,Void> putFile(String bucket, String fileId, FileSource source ) {

        try {

            if (!mc.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                mc.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            return CallResult.fail( new ExceptionMessage( e ) );
        }

        try {

            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder().bucket(bucket).object(fileId);


            if ( source.getContentType() != null ) {
                putObjectArgsBuilder.contentType(source.getContentType());
            }
            if ( source.getExtInfo() != null ) {
                putObjectArgsBuilder.extraHeaders(source.getExtInfo());
            }

            putObjectArgsBuilder.stream(  source.getInputStream()  , source.getObjectSize(),
                    source.getPartSize().isPresent() ? source.getPartSize().longValue() : ObjectWriteArgs.MAX_PART_SIZE );

            mc.putObject( putObjectArgsBuilder.build() );


            return CallResult.success();

        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            return CallResult.fail( new ExceptionMessage( e ) );

        } /* finally {
            StreamUtil.close( source );
        }*/
    }



    @Override
    public CallResult<ErrorMessage,FileMeta> getFileMeta(String bucket, String fileId) {


        try {
            StatObjectResponse stat = mc.statObject(StatObjectArgs.builder().bucket(bucket).object(fileId).build());

            FileMeta meta = new FileMeta( stat.bucket() , stat.object(),
                    stat.size(), stat.contentType(), stat.userMetadata() );

            return CallResult.success( meta );

        } catch ( ErrorResponseException e ) {

            LOG.error(StringUtil.EMPTY , e);

            if ( "NoSuchKey".equals(e.errorResponse().code())  ) {

                return CallResult.fail( new StatusMessage<>( GetFileStatus.NOT_FOUND , e.errorResponse().code() , e.errorResponse().message() ) );

            } else {

                return CallResult.fail( new StatusMessage<>( GetFileStatus.FAIL , e.errorResponse().code() , e.errorResponse().message() ) );

            }

        } catch ( Exception e ) {

            LOG.error(StringUtil.EMPTY , e);

            return CallResult.fail( new ExceptionMessage( e ) );

        }

    }

    @Override
    public CallResult<ErrorMessage,FileObject> getFile(String bucket , String fileId ) {

        BufferedInputStream fileIn = null;
        try {

            fileIn = new BufferedInputStream( mc.getObject( GetObjectArgs.builder().bucket(bucket).object(fileId).build() ) );

            byte[] fileContent = h2o.jodd.io.IOUtil.readBytes(fileIn);
            FileObject fileObject = new FileObject( fileContent );

            StatObjectResponse stat = mc.statObject(StatObjectArgs.builder().bucket(bucket).object(fileId).build());

            fileObject.setContentType( stat.contentType() );
            fileObject.setExtInfo( stat.userMetadata() );

            return CallResult.success( fileObject );

        } catch ( ErrorResponseException e ) {

            LOG.error(StringUtil.EMPTY , e);

            if ( "NoSuchKey".equals(e.errorResponse().code())  ) {

                return CallResult.fail( new StatusMessage<>( GetFileStatus.NOT_FOUND , e.errorResponse().code() , e.errorResponse().message() ) );

            } else {

                return CallResult.fail( new StatusMessage<>( GetFileStatus.FAIL , e.errorResponse().code() , e.errorResponse().message() ) );

            }

        } catch ( Exception e ) {

            LOG.error(StringUtil.EMPTY , e);

            return CallResult.fail( new ExceptionMessage( e ) );

        } finally {
            StreamUtil.close( fileIn );
        }
    }



    @Override
    public void output(String bucket, String fileId, FileConsumer consumer ) {

        FileSource source = null;
        try {

            StatObjectResponse stat = mc.statObject(StatObjectArgs.builder().bucket(bucket).object(fileId).build());

            GetObjectResponse objectResponse = mc.getObject(GetObjectArgs.builder().bucket(bucket).object(fileId).build());

            source = new FileSource( objectResponse , stat.size() );

            source.setContentType( stat.contentType() );
            source.setExtInfo( stat.userMetadata() );

            consumer.accept( source );

        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            throw ExceptionUtil.toRuntimeException( e );
        } finally {
            StreamUtil.close( source );
        }
    }




    @Override
    public CallResult<ErrorMessage,Void> delFile(String bucket, String fileId) {

        try {

            mc.removeObject( RemoveObjectArgs.builder().bucket(bucket).object(fileId).build() );

            return CallResult.success();

        } catch ( Exception e ) {
            LOG.error(StringUtil.EMPTY , e);
            return CallResult.fail( new ExceptionMessage( e ) );
        }

    }

}
