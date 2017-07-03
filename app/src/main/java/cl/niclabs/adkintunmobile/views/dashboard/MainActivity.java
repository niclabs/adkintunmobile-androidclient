package cl.niclabs.adkintunmobile.views.dashboard;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cl.niclabs.adkintunmobile.BuildConfig;
import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;
import cl.niclabs.adkintunmobile.services.SetupSystem;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
import cl.niclabs.adkintunmobile.utils.display.NotificationManager;
import cl.niclabs.adkintunmobile.utils.display.ShowCaseTutorial;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.views.activeconnections.ActiveConnectionsActivity;
import cl.niclabs.adkintunmobile.views.activemeasurements.ActiveMeasurementsActivity;
import cl.niclabs.adkintunmobile.views.applicationstraffic.ApplicationsTrafficActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.connectionmode.ConnectionModeActivity;
import cl.niclabs.adkintunmobile.views.connectiontype.networktype.NetworkTypeActivity;
import cl.niclabs.adkintunmobile.views.notificationlog.NotificationLogActivity;
import cl.niclabs.adkintunmobile.views.settings.SettingsActivity;
import cl.niclabs.adkintunmobile.views.status.DataQuotaDialog;
import cl.niclabs.adkintunmobile.views.status.DayOfRechargeDialog;
import cl.niclabs.adkintunmobile.views.status.StatusActivity;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private final String TAG = "AdkM:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!BuildConfig.DEBUG_MODE)
            Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        this.context = this;

        // Start System
        SetupSystem.startUpSystem(this.context);

        // Initial Fragment: DashboardFragment
        DashboardFragment mDashboardFragment;
        if (savedInstanceState != null){
            mDashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("DashboardFragment");
        } else {
            mDashboardFragment = new DashboardFragment();
        }
        updateMainFragment(mDashboardFragment);

        // Show tutorial
        showTutorial();
        if (needsGranted()) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.view_dashboard_request_permission_title))
                    .setMessage(getString(R.string.view_dashboard_request_permission_message))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        }
    }

    private boolean needsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean granted;
            AppOpsManager appOps = (AppOpsManager) context
                    .getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), context.getPackageName());

            if (mode == AppOpsManager.MODE_DEFAULT) {
                granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
            } else {
                granted = (mode == AppOpsManager.MODE_ALLOWED);
            }
            return !granted;
        }
        else
            return false;
    }

    /***
     * Abre vista de notificaciones. No disponible para el usuario.
     */
    public void openNotificationView(View view){
        Intent myIntent = new Intent(getApplicationContext(), NotificationLogActivity.class);
        startActivity(myIntent);
    }

    public void openStatusView(View view){
        Intent myIntent = new Intent(getApplicationContext(), StatusActivity.class);
        startActivity(myIntent);
    }

    public void openApplicationTrafficView(View view){
        Intent myIntent = new Intent(getApplicationContext(), ApplicationsTrafficActivity.class);
        startActivity(myIntent);
    }

    public void openConnectionModeView(View view){
        Intent myIntent = new Intent(getApplicationContext(), ConnectionModeActivity.class);
        startActivity(myIntent);
    }

    public void openNetworkTypeView(View view){
        Intent myIntent = new Intent(getApplicationContext(), NetworkTypeActivity.class);
        startActivity(myIntent);
    }

    public void openActiveConnectionsView(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ActiveConnectionsActivity.class);
        startActivity(myIntent);
    }

    public void openActiveMeasurementsView(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ActiveMeasurementsActivity.class);
        startActivity(myIntent);
    }

    public void openPreferencesView(View view) {
        Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(myIntent);
    }

    public void openAdkintunWebView(View view) {
        checkReadPhonePermission();
    }

    /*
     * changeCurrentFragment Methods
     */
    public void updateMainFragment(Fragment newFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);

        fragmentTransaction.replace(R.id.main_content, newFragment, "DashboardFragment");
        fragmentTransaction.commit();
    }

    private void checkReadPhonePermission() {
        final IntentIntegrator scanIntegrator = new IntentIntegrator(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            scanIntegrator.setPrompt("");
            scanIntegrator.setBeepEnabled(false);
            scanIntegrator.setCaptureActivity(CaptureCodeActivity.class);
            scanIntegrator.initiateScan();
        }
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

    public void notif(View view){

        long[] dailyData = ApplicationTraffic.getTransferedData(ApplicationTraffic.MOBILE, DisplayDateManager.timestampAtStartDay(System.currentTimeMillis()));
        String dataUsage = Network.formatBytes(dailyData[0] + dailyData[1]);
        String date = DisplayDateManager.getDateString(System.currentTimeMillis(), new SimpleDateFormat("dd/MM"));
        String title = getString(R.string.notification_daily_report_title);
        String body = String.format(getString(R.string.notification_daily_report_body) , date, dataUsage);

        NewsNotification notification = new NewsNotification(NewsNotification.INFO, title, body);
        notification.save();

        NewsNotification n = NewsNotification.findFirst(
                NewsNotification.class,
                "timestamp = ?",
                NewsNotification.mostRecentlyTimestamp()+"");
        NotificationManager.showNotification(this, n.title, n.content, new Intent(context, ApplicationsTrafficActivity.class));
    }

    private int helpCounter;
    private ShowcaseView showcaseView;

    private void showTutorial() {
        helpCounter = 0;
        final String[] tutorialTitle = getResources().getStringArray(R.array.tutorial_dashboard_title);
        final String[] tutorialBody = getResources().getStringArray(R.array.tutorial_dashboard_body);

        Display display = getWindowManager().getDefaultDisplay();
        Point firstPoint = new Point();
        display.getSize(firstPoint);
        firstPoint.y = (int) getResources().getDimension(R.dimen.dashboard_first_target_y);
        firstPoint.x /= 2;
        Target firstTarget = new PointTarget(firstPoint);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpCounter++;
                Target mTarget;

                switch (helpCounter) {
                    case 1:
                        mTarget = new ViewTarget(findViewById(R.id.shimmer_dashboard_logo));
                        break;

                    case 2:
                        mTarget = new ViewTarget(findViewById(R.id.tv_antenna));
                        break;

                    case 3:
                        mTarget = new ViewTarget(findViewById(R.id.tv_sim));
                        break;

                    case 4:
                        mTarget = new ViewTarget(findViewById(R.id.tv_signal));
                        break;

                    case 5:
                        mTarget = new ViewTarget(findViewById(R.id.tv_internet));
                        break;

                    case 6:
                        mTarget = new ViewTarget(findViewById(R.id.card_mobile_consumption));
                        break;

                    case 7:
                        mTarget = new ViewTarget(findViewById(R.id.iv_collapsable_toolbar_app_icon));
                        showcaseView.setButtonText(getString(R.string.tutorial_close));
                        break;

                    default:
                        showcaseView.hide();
                        final FragmentManager fm = getSupportFragmentManager();
                        DataQuotaDialog.showDialogPreference(fm, new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                DayOfRechargeDialog.showDialogPreference(fm, null);
                            }
                        });
                        return;
                }
                showcaseView.setContentTitle(tutorialTitle[helpCounter]);
                showcaseView.setContentText(tutorialBody[helpCounter]);
                showcaseView.setShowcase(mTarget, true);
            }
        };

        showcaseView = ShowCaseTutorial.createInitialTutorial(this, firstTarget, tutorialTitle, tutorialBody, onClickListener);
    }
}

