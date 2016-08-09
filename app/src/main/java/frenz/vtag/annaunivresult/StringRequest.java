package frenz.vtag.annaunivresult;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by gunalan on 22/7/16.
 */
public class StringRequest extends Request<String> {

    public static String URL_ANNA_UNIV = "http://aucoe.annauniv.edu/cgi-bin/result/cgrade.pl?regno=";

    private Response.Listener<String> listener;
    private Map<String, String> params;

    /*public StringRequest(String url, Map<String, String> params,
                         Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

    public StringRequest(String url,
                         Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        //
        setRetryPolicy(new DefaultRetryPolicy(5* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    /*public StringRequest(int method, String url, Map<String, String> params,
                         Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }*/

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {

        return params;
    }



    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(string,
                    HttpHeaderParser.parseCacheHeaders(response));


        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}
