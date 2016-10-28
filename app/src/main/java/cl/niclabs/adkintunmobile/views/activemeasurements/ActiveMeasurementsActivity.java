package cl.niclabs.adkintunmobile.views.activemeasurements;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersDialog;
import cl.niclabs.adkintunmobile.utils.activemeasurements.ActiveServersTask;

public class ActiveMeasurementsActivity extends AppCompatActivity{

    private final String TAG = "AdkM:ActiveMeasurActivity";

    protected String title;
    protected Context context;
    protected Toolbar toolbar;

    private TextView urlsTime;
    private EditText serverUrl;
    private EditText editTextFileSize;
    private WebView webView;
    private int i = 0;

    static String[] serversUrl;

    public static String[] getServers() {
        return serversUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_measurements);

        setBaseActivityParams();
        setupToolbar();

        urlsTime = (TextView) findViewById(R.id.urls);
        webView = (WebView) findViewById(R.id.webView);
    }

    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth()/2; //in the middle of the webview
        float y = webview.getTop() + webview.getHeight()/2;

        final MotionEvent motionEvent = MotionEvent.obtain( downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0 );
        final MotionEvent motionEvent2 = MotionEvent.obtain( downTime + delta + 1, downTime + delta * 2, MotionEvent.ACTION_UP, x, y, 0 );
        Runnable tapdown = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent);
                }
            }
        };

        Runnable tapup = new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(motionEvent2);
                }
            }
        };

        int toWait = 0;
        int delay = 100;
        webview.postDelayed(tapdown, delay);
        delay += 100;
        webview.postDelayed(tapup, delay);

    }
    public void onSpeedTestClick(View view){
/*
        downloadGraph.removeAllSeries();
        uploadGraph.removeAllSeries();

        downloadSeries = new LineGraphSeries<DataPoint>();
        uploadSeries = new LineGraphSeries<DataPoint>();
        downloadGraph.addSeries(downloadSeries);
        uploadGraph.addSeries(uploadSeries);
*/

        selectServer();

        //startSpeedTest(fileOctetSize);
        //startWebPagesTest();
        //startVideoTest();
    }

    public void onWebPagesTestClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("webPagesTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        WebPagesTestDialog newFragment = new WebPagesTestDialog();
        newFragment.show(ft, "webPagesTestDialog");
    }


    public void onVideoTestClick(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("videoTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        VideoTestDialog newFragment = new VideoTestDialog();
        newFragment.show(ft, "videoTestDialog");
    }

    private void selectServer() {
        new ActiveServersTask(this).execute();
    }

    public void startSpeedTest(int fileOctetSize, String currentServer) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("speedTestDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        SpeedTestDialog newFragment = new SpeedTestDialog();
        newFragment.setSpeedTestParams(fileOctetSize, currentServer);
        newFragment.show(ft, "speedTestDialog");

        //new SpeedTest(this, fileOctetSize, currentServer).start();
    }


    public void setupToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(this.title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setBaseActivityParams(){
        this.title = getString(R.string.view_active_measurements);
        this.context = this;
    }

    public void onActiveServersReceived(String[] serversUrl) {
        this.serversUrl = serversUrl;
        FragmentManager fm = getSupportFragmentManager();
        ActiveServersDialog.showDialogPreference(fm, null);
    }
}
