package h2o.common.util.io;

import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.lang.tuple.TupleUtil;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class VFSStreamUtil {

    private static final Logger log = LoggerFactory.getLogger( VFSStreamUtil.class.getName() );

    private VFSStreamUtil() {
	}

	public static FileObject getFileObject(String path) {
		try {
			return VFS.getManager().resolveFile(path);
		} catch (FileSystemException e) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	public static Tuple2<InputStream, FileObject> getFileInputStream(String path) {
		FileObject fo = getFileObject(path);
		try {
			return TupleUtil.t2(fo.getContent().getInputStream(), fo);
		} catch (FileSystemException e) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	public static Tuple2<OutputStream, FileObject> getFileOutputStream(String path, boolean append) {
		FileObject fo = getFileObject(path);
		try {
			return TupleUtil.t2(fo.getContent().getOutputStream(append), fo);
		} catch (FileSystemException e) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	public static Tuple2<Reader, FileObject> readFile(String path) {
		return readFile(path, null);
	}

	public static Tuple2<Reader, FileObject> readFile(String path, String characterEncoding) {

		log.debug("readFile path -- {}", path);

		Tuple2<InputStream, FileObject> input = getFileInputStream(path);

		return TupleUtil.t2(StreamUtil.toReader(input.e0, characterEncoding), input.e1);

	}

	public static Reader readFile(FileObject f) {
		return readFile(f, null);
	}

	public static Reader readFile(FileObject f, String characterEncoding) {

		InputStream input;
		try {
			input = f.getContent().getInputStream();
		} catch (FileSystemException e) {
			throw ExceptionUtil.toRuntimeException(e);
		}

		return StreamUtil.toReader(input, characterEncoding);

	}

	public static String readFileContent(String path) {
		return readFileContent(path, null);
	}

	public static String readFileContent(String path, String characterEncoding) {

		Tuple2<Reader, FileObject> r = readFile(path, characterEncoding);

		try {
			return new String( h2o.jodd.io.IOUtil.readChars(r.e0));
		} catch (IOException e) {
			log.debug("readFileContent", e);
			throw ExceptionUtil.toRuntimeException(e);
		} finally {
			
			StreamUtil.close(r.e0);
			closeFileObject(r.e1);
		}

	}

	public static String readFileContent(FileObject f) {
		return readFileContent(f, null);
	}

	public static String readFileContent(FileObject f, String characterEncoding) {		
		return StreamUtil.readReaderContent(readFile(f, characterEncoding), true);		
	}

	public static Tuple2<Writer, FileObject> writeFile(String path) {
		return writeFile(path, null, false);
	}

	public static Tuple2<Writer, FileObject> writeFile(String path, boolean append) {
		return writeFile(path, null, append);
	}

	public static Tuple2<Writer, FileObject> writeFile(String path, String characterEncoding) {
		return writeFile(path, characterEncoding, false);
	}

	public static Tuple2<Writer, FileObject> writeFile(String path, String characterEncoding, boolean append) {

		log.debug("writeFile path -- {}", path);

		Tuple2<OutputStream, FileObject> out = getFileOutputStream(path, append);

		return TupleUtil.t2(StreamUtil.toWriter(out.e0, characterEncoding), out.e1);

	}

	public static Writer writeFile(FileObject f) {
		return writeFile(f, null, false);
	}

	public static Writer writeFile(FileObject f, boolean append) {
		return writeFile(f, null, append);
	}

	public static Writer writeFile(FileObject f, String characterEncoding) {
		return writeFile(f, characterEncoding, false);
	}

	public static Writer writeFile(FileObject f, String characterEncoding, boolean append) {

		OutputStream out;
		try {
			out = f.getContent().getOutputStream(append);
		} catch (FileSystemException e) {
			throw ExceptionUtil.toRuntimeException(e);
		}

		return StreamUtil.toWriter(out, characterEncoding);

	}
	
	
	public static void closeFileObject(FileObject f) {
		if (f != null) {
			try {
				f.close();
			} catch (Exception e) {
				log.debug("fileObject.close()", e);
			}
		}
	}

}
