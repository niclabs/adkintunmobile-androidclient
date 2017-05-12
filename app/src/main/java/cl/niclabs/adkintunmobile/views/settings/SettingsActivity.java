package cl.niclabs.adkintunmobile.views.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cl.niclabs.adkintunmobile.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "AdkM:SettingsActivity";

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

        if(data != null && data.getAction() != null && data.getAction().equals(ACTION_SCAN)) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String qrString = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();

                String qrCodeFormat = BarcodeFormat.QR_CODE.toString();
                if (scanFormat.equals(qrCodeFormat)) {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String deviceId = telephonyManager.getDeviceId();
                    sendToServer(deviceId, qrString);
                    showText(getString(R.string.settings_adkintun_web_qr_scanner_success));
                }
                else
                    showText(getString(R.string.settings_adkintun_web_qr_scanner_failure));

                super.onActivityResult(requestCode, resultCode, data);
            }
            else {
                showText(getString(R.string.settings_adkintun_web_qr_scanner_failure));
            }

        }
    }

    private void sendToServer(String accessToken,String qrString) {
        String url = getString(R.string.web_auth_url);

        JSONObject params = new JSONObject();
        try {
            params.put("uuid", qrString);
            params.put("access_token", accessToken);
            StringEntity entity = new StringEntity(params.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, statusCode + " OK");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(TAG, statusCode + " NOT OK");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void showText(String message){
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
