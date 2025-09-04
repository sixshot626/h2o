package h2o.common.util.io;

import h2o.common.io.CharsetWrapper;
import h2o.common.lang.NBytes;

import java.io.*;
import java.util.function.Consumer;

public class ByteArrayStore {

    private volatile NBytes bytes = NBytes.NULL;

    private final CharsetWrapper charsetWrapper;


    public ByteArrayStore() {
        this.charsetWrapper = CharsetWrapper.UNSET;
    }

    public ByteArrayStore(CharsetWrapper charsetWrapper) {
        this.charsetWrapper = charsetWrapper;
    }


    public void out( Consumer<ByteArrayOutputStream> consumer ) throws IOException {


        try (
                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ) {

            consumer.accept( bytesOut );
            bytesOut.flush();

            bytes = new NBytes( bytesOut.toByteArray() );

        }


    }


    public void in( Consumer<ByteArrayInputStream> consumer ) throws IOException {

        try (
                ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes.get());
        ) {
            consumer.accept( bytesIn );
        }

    }



    public void write( Consumer<BufferedWriter> writerConsumer ) throws IOException {


        try (
                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter( StreamUtil.toWriter( bytesOut , charsetWrapper.charset ))
        ) {

            writerConsumer.accept( bufferedWriter );
            bufferedWriter.flush();

            bytes = new NBytes( bytesOut.toByteArray() );

        }


    }


    public void read( Consumer<BufferedReader> readConsumer ) throws IOException {

        try (
                ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes.get());
                BufferedReader bufferedReader = new BufferedReader( StreamUtil.toReader( bytesIn , charsetWrapper.charset ))
        ) {
            readConsumer.accept( bufferedReader );
        }

    }


    public NBytes getBytes() {
        return bytes;
    }

    public CharsetWrapper getCharset() {
        return charsetWrapper;
    }

}
