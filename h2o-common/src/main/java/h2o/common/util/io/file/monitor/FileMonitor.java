package h2o.common.util.io.file.monitor;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import h2o.common.Tools;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.util.io.StreamUtil;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileMonitor {

    private final IOFileFilter filter;

    private List<AlterationObserver> observers;

    private boolean reliableMode;

    private String saveFilePath;

    private FileChangeListener changeListener;

    private FileMonitor(FileFilter filter ) {
        IOFileFilter baseFilter = FileFilterUtils.or(FileFilterUtils.fileFileFilter(), FileFilterUtils.directoryFileFilter());
        if ( filter == null ) {
            this.filter = baseFilter;
        } else {
            this.filter = FileFilterUtils.and(baseFilter, new ProxyIOFileFilter( filter ) );
        }
    }


    public static FileMonitor monitor( String[] paths , FileFilter filter  ) throws Exception {

        FileMonitor fileMonitor = new FileMonitor(filter);

        fileMonitor.observers = ListBuilder.newList();

        for ( String path : paths ) {

            AlterationObserver observer = new AlterationObserver( path , fileMonitor.filter );

            observer.initialize();
            observer.addListener( fileMonitor.fileAlterationListener );

            fileMonitor.observers.add( observer );
        }

        return fileMonitor;
    }


    public static FileMonitor reliableMonitor( String[] paths , FileFilter filter , String saveFilePath , boolean reset ) throws Exception {

        FileMonitor fileMonitor = monitor( paths , filter  );
        fileMonitor.enableReliableMode( saveFilePath );

        if ( reset && fileMonitor.hasBackupFile() ) {

            fileMonitor.reset();

            if ( ! fileMonitor.diffPaths( paths ) ) {
                throw new IllegalArgumentException("The paths is different from backup data.");
            }

        } else {
            fileMonitor.save();
        }

        return fileMonitor;
    }


    public boolean diffPaths( String[] paths ) throws IOException {

        try {

            Set<String> paths1 = new HashSet<String>();
            for ( String path : paths ) {
                paths1.add( new File(path).getPath()  );
            }

            Set<String> paths2 = new HashSet<String>();
            for ( FileAlterationObserver observer : observers ) {
                paths2.add( observer.getDirectory().getPath()  );
            }

            return paths1.equals(paths2);

        } catch ( UncheckedException ioe ) {
            throw (IOException) ioe.getCause();
        }

    }


    public static FileMonitor loadByBackup(FileFilter filter , String saveFilePath  ) throws Exception {

        FileMonitor fileMonitor = new FileMonitor( filter );
        fileMonitor.enableReliableMode( saveFilePath );
        fileMonitor.reset();

        return fileMonitor;
    }


    public void enableReliableMode( String saveFilePath ) {
        this.saveFilePath = saveFilePath;
        reliableMode = true;
    }


    public void setChangeListener(FileChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void checkAndNotify() throws Exception {


        try {

            for ( FileAlterationObserver observer : observers ) {
                observer.checkAndNotify();
            }

        } catch ( UncheckedException e ) {

            if ( reliableMode ) {
                try {
                    reset();
                } catch ( Exception e2 ) {
                    Tools.log.error(e2);
                }

            }

            throw (Exception) e.getCause();

        } catch ( Exception e ) {

            if ( reliableMode ) {
                try {
                    reset();
                } catch ( Exception e2 ) {
                    Tools.log.error(e2);
                }
            }

            throw e;
        }

        if ( reliableMode ) {
            save();
        }



    }


    public void save() throws Exception {
        this.save( this.backupObj() );
    }


    protected void save( List<FileEntry> fileEntries ) throws Exception {

        OutputStream os = null;
        try {

            os = new BufferedOutputStream( new FileOutputStream( getTmpFilePath() ) );

            HessianOutput ho = new HessianOutput(os);
            ho.writeObject( fileEntries );

        } finally {
            StreamUtil.close(os);
        }

        File fileBak = new File( this.saveFilePath + ".bak" );
        if ( fileBak.exists() ) {
            fileBak.delete();
        }
        File file = new File( this.saveFilePath );
        if ( file.exists() ) {
            file.renameTo(fileBak);
        }
        new File( getTmpFilePath()  ).renameTo( file );

    }




    public void reset() throws Exception {

        if ( ! hasBackupFile() ) {
            throw new FileNotFoundException(this.saveFilePath);
        }

        try {

            this.observers = formBackupObj( load( this.saveFilePath ) );

        } catch ( Exception e ) {

            try {
                this.observers = formBackupObj(load( getTmpFilePath() ) );
            } catch ( Exception e2 ) {
                Tools.log.error( e2 );
                throw e;
            }

            Tools.log.error( e );
        }

    }



    protected boolean hasBackupFile() {
        return new File( this.saveFilePath ).exists() || new File( this.getTmpFilePath() ).exists();
    }


    protected String getTmpFilePath() {
        return this.saveFilePath + ".tmp";
    }



    private List<AlterationObserver> formBackupObj( List<FileEntry> entries ) {

        List<AlterationObserver> observers = ListBuilder.newList( entries.size() );

        for ( FileEntry entity : entries ) {

            AlterationObserver observer = new AlterationObserver( entity , filter );
            observer.addListener( fileAlterationListener );

            observers.add( observer );
        }

        return observers;
    }


    private List<FileEntry> backupObj() {

        List<FileEntry> fileEntries = ListBuilder.newList( observers.size() );

        for ( AlterationObserver observer : observers ) {
            fileEntries.add( observer.getEntry()  );
        }

        return fileEntries;
    }

    protected List<FileEntry> load( String path ) throws IOException {

        InputStream is = null;
        try {

            is = new BufferedInputStream( new FileInputStream( path ) );

            HessianInput hi = new HessianInput(is);
            return (List<FileEntry>)hi.readObject();

        } finally {
            StreamUtil.close(is);
        }

    }



    ////////////////////////////////////////////////////////////////////////////////////////////


    private static class AlterationObserver extends FileAlterationObserver {

        private final FileEntry entry;

        public AlterationObserver(String path, FileFilter fileFilter) {
            this( new FileEntry( new File( path) ) , fileFilter );
        }

        public AlterationObserver(FileEntry rootEntry, FileFilter fileFilter ) {
            super(rootEntry, fileFilter, null);
            this.entry = rootEntry;
        }

        public FileEntry getEntry() {
            return entry;
        }
    }







    private final FileAlterationListener fileAlterationListener = new FileAlterationListener() {

        @Override
        public void onStart(FileAlterationObserver observer) {
        }

        @Override
        public void onDirectoryCreate(File directory) {
            if ( changeListener != null  ) try {
                changeListener.onDirectoryCreate( directory );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onDirectoryChange(File directory) {
            if ( changeListener != null  ) try {
                changeListener.onDirectoryChange( directory );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onDirectoryDelete(File directory) {
            if ( changeListener != null  ) try {
                changeListener.onDirectoryDelete( directory );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onFileCreate(File file) {
            if ( changeListener != null  ) try {
                changeListener.onFileCreate( file );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onFileChange(File file) {
            if ( changeListener != null  ) try {
                changeListener.onFileChange( file );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onFileDelete(File file) {
            if ( changeListener != null  ) try {
                changeListener.onFileDelete( file );
            } catch ( Exception e ) {
                throw new UncheckedException(e);
            }
        }

        @Override
        public void onStop(FileAlterationObserver observer) {
        }
    };


    private static class UncheckedException extends RuntimeException {
        public UncheckedException(Throwable cause) {
            super(cause);
        }
    }


    private static class ProxyIOFileFilter extends AbstractFileFilter implements IOFileFilter {

        private final FileFilter fileFilter;

        public ProxyIOFileFilter( FileFilter fileFilter) {
            this.fileFilter = fileFilter;
        }

        @Override
        public boolean accept(File file) {
            return fileFilter.accept( file );
        }


    }


}
