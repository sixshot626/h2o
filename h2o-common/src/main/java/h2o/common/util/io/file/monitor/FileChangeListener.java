package h2o.common.util.io.file.monitor;

import java.io.File;


public interface FileChangeListener {


    void onDirectoryCreate(final File directory) throws Exception;


    void onDirectoryChange(final File directory) throws Exception;


    void onDirectoryDelete(final File directory) throws Exception;


    void onFileCreate(final File file) throws Exception;


    void onFileChange(final File file) throws Exception;


    void onFileDelete(final File file) throws Exception;


}
