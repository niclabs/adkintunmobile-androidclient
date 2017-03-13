package cl.niclabs.adkintunmobile.views.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cl.niclabs.adkintunmobile.R;
import cz.msebera.android.httpclient.Header;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.context = this;

        setupToolBar();

        // display fragment for settings
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, new SettingsFragment()).commit();
    }

    /**
     * Setup toolbar as an actionbar
     */
    private void setupToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(getString(R.string.view_settings));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v("####","Request:"+requestCode+ "resultCOde:"+resultCode+" Data:"+data);
        if(data != null && data.getAction() != null && data.getAction().equals(ACTION_SCAN)) //QR Code
        {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String qrStr = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();

                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = telephonyManager.getDeviceId();
                sendToServer(deviceId, qrStr);

                super.onActivityResult(requestCode, resultCode, data);
                //we have a result
            }
            else {
                showText("No QR scan data received");
            }

        }
    }

    private void sendToServer(String accessToken,String qrStr) {
        String url = "http://172.30.65.189:8080/auth";

        RequestParams params = new RequestParams();
        params.put("uuid", qrStr);
        params.put("access_token", accessToken);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("SUCCESS", statusCode + " OK");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUCCESS", statusCode + "NOT OK");
            }
        });
    }

    private void showText(String message){
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
