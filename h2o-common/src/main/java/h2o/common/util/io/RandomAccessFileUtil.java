package h2o.common.util.io;

import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;

public final class RandomAccessFileUtil {

    private static final Logger log = LoggerFactory.getLogger(RandomAccessFileUtil.class.getName());

    private RandomAccessFileUtil() {
    }

    public static void create(String path, long size) {

        RandomAccessFile accessFile = null;
        try {

            accessFile = new RandomAccessFile(path, "rwd");
            accessFile.setLength(size);

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            close(accessFile);
        }

    }

    public static long getFileLength(String path) {

        RandomAccessFile accessFile = null;
        try {

            accessFile = new RandomAccessFile(path, "r");
            return accessFile.length();

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            close(accessFile);
        }

    }

    public static void writePart(String path, long start, byte[] data, int off, int len) {

        RandomAccessFile accessFile = null;
        try {

            accessFile = new RandomAccessFile(path, "rw");
            accessFile.seek(start);
            accessFile.write(data, off, len);

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            close(accessFile);
        }

    }

    public static int readPart(String path, long start, byte[] data, int off, int len) {

        RandomAccessFile accessFile = null;

        try {

            accessFile = new RandomAccessFile(path, "r");

            long l = accessFile.length();

            if (start >= l) {

                return -1;

            } else {

                accessFile.seek(start);
                return accessFile.read(data, off, len);
            }

        } catch (Exception e) {

            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);

        } finally {
            close(accessFile);
        }

    }

    private static void close(RandomAccessFile accessFile) {
        if (accessFile != null) {
            try {
                accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
