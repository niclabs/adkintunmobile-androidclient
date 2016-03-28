package cl.niclabs.adkintunmobile.utils.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpPostRequest extends Request<JSONObject>{
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    /**
     * Creates a new POST Request based on a Map object.
     *
     * HttpPostRequest structure:

          We need a Map<String,String> object to create the request, each pair in the map will be considered
          as a name/value pair in the JSON request.
              Map<String, String> params;


          HttpPostRequest request = new HttpPostRequest( "example.com", params, new Response.Listener<JSONObject>(){
              @Override
              public void onResponse(JSONObject response) {
                  //Method to handle the response
              }
          }, new Response.ErrorListener(){
              @Override
              public void onErrorResponse(VolleyError error) {
                  //Method to handle the error
              }
          });

          //Add the request to the Request Queue
          VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);

     * @param url               URL to fetch the file at
     * @param params            Request parameters and values
     * @param responseListener  Listener to receive the response
     * @param errorListener     Error listener, or null to ignore errors
     */
    public HttpPostRequest(String url, Map<String, String> params,
                           Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
