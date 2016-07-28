package cl.niclabs.adkintunmobile.utils.compression;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

public class CompressionUtils {

    public enum CompressionType{
        NOCOMPRESSION, GZIP, ZIPDEFLATER
    }

    private static final String TAG = "AdkM:CompressionUtils";

    /**
     * Compresses a byte array using gzip
     * @param bytes A byte array from the object to compress
     * @return A byte array result of the compression process
     * @throws IOException
     */
    public static byte[] gzip(byte[] bytes) throws IOException{
        ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;
        try {
            gzos = new GZIPOutputStream(mByteArrayOutputStream);
            gzos.write(bytes);
        }finally {
            if (gzos != null){
                try{
                    gzos.close();
                }catch (IOException ignore) {}
            }
        }
        Log.d(TAG, "Original: " + bytes.length);
        Log.d(TAG, "Compressed: " + mByteArrayOutputStream.toByteArray().length);
        return mByteArrayOutputStream.toByteArray();
    }

    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        Log.d(TAG, "Original: " + data.length  + " b");
        Log.d(TAG, "Compressed: " + output.length + " b");
        return output;
    }
    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        Log.d(TAG, "Original: " + data.length);
        Log.d(TAG, "Compressed: " + output.length);
        return output;
    }

}
