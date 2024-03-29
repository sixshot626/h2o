package h2o.common.util.io;

import h2o.apache.commons.configuration.ConfigurationUtils;
import h2o.common.exception.ExceptionUtil;
import h2o.common.io.CharsetWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class StreamUtil {

    private static final Logger log = LoggerFactory.getLogger(StreamUtil.class.getName());

    private StreamUtil() {
    }

    public static Reader readFile(String path) {
        return readFile(path, null);
    }

    public static Reader readFile(String path, String characterEncoding) {
        return toReader(openStream(path), characterEncoding);
    }


    public static InputStream openStream(String path) {


        try {
            return getURL(path).openStream();
        } catch (IOException e) {
            log.debug("openStream", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static URL getURL(String path) {

        URL url = ConfigurationUtils.locate(path);

        log.info("Find [{}] ===> {}", path, url);

        return url;

    }


    public static Reader readFile(File f) {
        return readFile(f, null);
    }

    public static Reader readFile(File f, String characterEncoding) {

        try {
            return toReader(new FileInputStream(f), characterEncoding);
        } catch (FileNotFoundException e) {
            log.debug("readFile", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

    public static String readFileContent(String path) {
        return readFileContent(path, null);
    }

    public static String readFileContent(String path, String characterEncoding) {
        return readReaderContent(readFile(path, characterEncoding), true);
    }

    public static String readFileContent(File f) {
        return readFileContent(f, null);
    }

    public static String readFileContent(File f, String characterEncoding) {
        return readReaderContent(readFile(f, characterEncoding), true);
    }


    public static String readReaderContent(Reader r, boolean closeReader) {

        try {

            return new String(h2o.jodd.io.IOUtil.readChars(r));

        } catch (IOException e) {
            log.debug("readReaderContent", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            if (closeReader) {
                close(r);
            }
        }
    }


    public static InputStream string2InputStream(String data, CharsetWrapper charsetWrapper) {
        try {
            return new ByteArrayInputStream(data.getBytes(charsetWrapper.charset));
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    public static Reader toReader(InputStream is) {
        return toReader(is, null);
    }

    public static Reader toReader(InputStream is, String characterEncoding) {
        try {
            if (characterEncoding == null) {
                return new java.io.InputStreamReader(is);
            } else {
                return new java.io.InputStreamReader(is, characterEncoding);
            }
        } catch (IOException e) {
            log.debug("toReader", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static Writer writeFile(String path) {
        return writeFile(path, null, false);
    }

    public static Writer writeFile(String path, boolean append) {
        return writeFile(path, null, append);
    }

    public static Writer writeFile(String path, String characterEncoding) {
        return writeFile(path, characterEncoding, false);
    }

    public static Writer writeFile(String path, String characterEncoding, boolean append) {

        log.debug("writeFile path -- {}", path);
        return writeFile(new File(path), characterEncoding, append);

    }

    public static Writer writeFile(File f) {
        return writeFile(f, null, false);
    }

    public static Writer writeFile(File f, boolean append) {
        return writeFile(f, null, append);
    }

    public static Writer writeFile(File f, String characterEncoding) {
        return writeFile(f, characterEncoding, false);
    }

    public static Writer writeFile(File f, String characterEncoding, boolean append) {

        try {
            return toWriter(new FileOutputStream(f, append), characterEncoding);
        } catch (FileNotFoundException e) {
            log.debug("writeFile", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

    public static Writer toWriter(OutputStream os) {
        return toWriter(os, null);
    }

    public static Writer toWriter(OutputStream os, String characterEncoding) {
        try {
            if (characterEncoding == null) {
                return new OutputStreamWriter(os);
            } else {
                return new OutputStreamWriter(os, characterEncoding);
            }
        } catch (IOException e) {
            log.debug("toWriter", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    public static void close(Closeable closeable) {
        if (closeable != null) {
            if (closeable instanceof Flushable) {
                try {
                    ((Flushable) closeable).flush();
                } catch (IOException e) {
                    log.error("close", e);
                }
            }

            try {
                closeable.close();
            } catch (IOException e) {
                log.error("close", e);
            }
        }
    }


}
