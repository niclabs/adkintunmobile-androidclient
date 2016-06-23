package cl.niclabs.adkintunmobile.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;


public class HttpMultipartRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;
    private final byte[] mMultipartBody;

    /**
     * Creates a new POST Request to send files and data over to a HTTP Server.
     *
     * HttpMultipartRequest structure:

     We need the corresponding byte-array of the file "example.zip" to be sent to de URL "example.com"
     byte[] file;

     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     DataOutputStream dos = new DataOutputStream(bos);
     try{
     MultipartRequest.buildPart(dos, file, "example.zip");   //    If you need to add another file to the Request, you can do:
     //    MultipartRequest.buildPart(dos, file2, "file2.zip");
     MultipartRequest.multipartFileDataSeparator(dos);
     byte[] multipartBody = bos.toByteArray();               //    Create multipart body
     }
     catch (IOException e){
     e.printStackTrace();
     }

     MultipartRequest request = new MultipartRequest( "example.com", null, multipartBody, new Response.Listener<NetworkResponse>(){
    @Override
    public void onResponse(NetworkResponse response) {
    //Method to handle the response
    }
    }, new Response.ErrorListener(){
    @Override
    public void onErrorResponse(VolleyError error) {
    //Method to handle the error
    }
    });

     //Add the request to the Request Queue
     VolleySingleton.getInstance(context).addToRequestQueue(request);

     * @param url               URL to fetch the file at
     * @param headers           Request headers
     * @param multipartBody     Body of multipart request to be sent
     * @param responseListener  Listener to receive the response
     * @param errorListener     Error listener, or null to ignore errors
     */
    public HttpMultipartRequest(
            String url,
            Map<String, String> headers,
            byte[] multipartBody,
            Response.Listener<NetworkResponse> responseListener,
            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = responseListener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
        this.mMultipartBody = multipartBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mMultipartBody;
        /**
         * Code block to send the request gzipped.
         * Must be added as in the request header "Content-Encoding", "gzip"
         try {
         return CompressionUtils.gzip(mMultipartBody);
         } catch (IOException e) {
         e.printStackTrace();
         return mMultipartBody;
         }
         */
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }


    /**
     * Static Methods
     */

    private static final String twoHyphens = "--";
    private static final String lineEnd = "\r\n";
    private static final String boundary = "apiclient-" + System.currentTimeMillis();
    private static final String mimeType = "multipart/form-data;boundary=" + boundary;

    /**
     * Build the body of a post http request, attaching a custom file.
     * @param dataOutputStream Stream where write the output
     * @param postParamFileName Parameter name in the post request where is located the file
     * @param fileData byteArray of the file to attach
     * @param fileName   @throws IOException
     */
    public static void buildPart(
            DataOutputStream dataOutputStream,
            String postParamFileName,
            byte[] fileData,
            String fileName) throws IOException {

        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

        String postParams ="";
        postParams += "Content-Disposition: form-data; ";
        postParams += "name=\"" + postParamFileName + "\"; ";
        postParams += "filename=\"" + fileName + "\"";
        postParams += lineEnd;

        dataOutputStream.writeBytes(postParams);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);

        // Close the request with a separator for multipart.
        // To send more files the previous process should be repeated.
        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
    }
}